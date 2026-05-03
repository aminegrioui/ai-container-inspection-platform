
import os
import time
import torch
import cv2
import json
import numpy as np
from PIL import Image
from torchvision import transforms
from torchvision.models import efficientnet_b0, EfficientNet_B0_Weights
from ultralytics import YOLO
from transformers import TrOCRProcessor, VisionEncoderDecoderModel

class ContainerIntelligenceCore:
    def __init__(
        self,
        s1_path,
        s2_path,
        trocr_path,
        yolo_conf=0.10,
        yolo_imgsz=1280,
        yolo_iou=0.50,
        text_label_names=("text",),
        ocr_pad=30,
        ocr_min_text_len=2,
    ):
        # 1. Device
        self.device = torch.device("cuda:0" if torch.cuda.is_available() else 
                                   "mps" if torch.backends.mps.is_available() else "cpu")
        print(f"Pipeline initialisiert auf: {self.device}")

        # 2. Stage 1: EfficientNet laden
        self.model_s1 = self._load_efficientnet(s1_path)

        # 3. Stage 2: YOLO laden
        self.model_s2 = YOLO(s2_path)

        # 4. Stage 3: TrOCR laden
        print(f"Lade TrOCR von {trocr_path}...")
        self.trocr_processor = TrOCRProcessor.from_pretrained(trocr_path, use_fast=False)
        self.trocr_model = VisionEncoderDecoderModel.from_pretrained(trocr_path).to(self.device)
        self.trocr_model.eval()

        # Konfiguration
        self.yolo_conf = float(yolo_conf)
        self.yolo_imgsz = int(yolo_imgsz)
        self.yolo_iou = float(yolo_iou)
        self.text_label_names = tuple(text_label_names)
        self.ocr_pad = int(ocr_pad)
        self.ocr_min_text_len = int(ocr_min_text_len)

        # Bild-Transform für Stage 1
        self.transform = transforms.Compose([
            transforms.Resize(224),
            transforms.CenterCrop(224),
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
        ])
        print("--- Alle Modelle geladen und bereit ---")

    def _load_efficientnet(self, weight_path, num_classes=2):
        weights = EfficientNet_B0_Weights.DEFAULT
        model = efficientnet_b0(weights=weights)
        in_features = model.classifier[1].in_features
        model.classifier[1] = torch.nn.Linear(in_features, num_classes)
        model.load_state_dict(torch.load(weight_path, map_location=self.device))
        model.to(self.device)
        model.eval()
        return model

    def _clean_text(self, text: str) -> str:
        return "".join(c for c in text if c.isalnum()).upper()

    def _run_trocr(self, crop):
        crop_rgb = cv2.cvtColor(crop, cv2.COLOR_BGR2RGB)
        pil_img = Image.fromarray(crop_rgb)
        pixel_values = self.trocr_processor(pil_img, return_tensors="pt").pixel_values.to(self.device)

        with torch.no_grad():
            ids = self.trocr_model.generate(pixel_values, max_new_tokens=32)
        
        text = self.trocr_processor.batch_decode(ids, skip_special_tokens=True)[0]
        return self._clean_text(text)

    def _crop_with_padding(self, img, x1, y1, x2, y2, pad):
        H, W = img.shape[:2]
        x1p, y1p = max(0, x1 - pad), max(0, y1 - pad)
        x2p, y2p = min(W, x2 + pad), min(H, y2 + pad)
        return img[y1p:y2p, x1p:x2p], [x1p, y1p, x2p, y2p]

    def prozess(self, bild_pfad):
        start_ms = time.time()
        img_cv2 = cv2.imread(bild_pfad)
        if img_cv2 is None:
            return {"status": "ERROR", "grund": "Bild nicht lesbar"}

        # --- Stage 1: Klassifizierung ---
        img_pil = Image.open(bild_pfad).convert('RGB')
        input_t = self.transform(img_pil).unsqueeze(0).to(self.device)

        with torch.no_grad():
            output = torch.softmax(self.model_s1(input_t), dim=1)
            conf_s1 = round(float(output[0][0]), 4)
            prediction = torch.argmax(output, dim=1).item()

        if prediction != 0:
            return {"status": "ABBRUCH", "grund": "Kein Container", "s1_conf": conf_s1}

        # --- Stage 2: YOLO Detektion ---
        results = self.model_s2(
            img_cv2, conf=self.yolo_conf, iou=self.yolo_iou, imgsz=self.yolo_imgsz, verbose=False
        )[0]

        funde = []
        text_eintraege = []
        text_coords = []

        for box in results.boxes:
            label = self.model_s2.names[int(box.cls[0])]
            conf_yolo = round(float(box.conf[0]), 2)
            x1, y1, x2, y2 = [int(c) for c in box.xyxy[0].tolist()]

            eintrag = {"klasse": label, "yolo_conf": conf_yolo, "box": [x1, y1, x2, y2], "stufe_3_aktiv": False}

            if label in self.text_label_names:
                text_eintraege.append(eintrag)
                text_coords.append((x1, y1, x2, y2))
            else:
                eintrag["ergebnis"] = "Hauptkomponente" if label == "container" else f"Warnetikett: {label}"
                funde.append(eintrag)

        # --- Stage 3: OCR ---
        if text_eintraege:
            all_x1 = min(c[0] for c in text_coords)
            all_y1 = min(c[1] for c in text_coords)
            all_x2 = max(c[2] for c in text_coords)
            all_y2 = max(c[3] for c in text_coords)

            combined_crop, padded_box = self._crop_with_padding(img_cv2, all_x1, all_y1, all_x2, all_y2, self.ocr_pad)
            
            if combined_crop.size > 0:
                ocr_text = self._run_trocr(combined_crop)
                is_valid = ocr_text and len(ocr_text) > self.ocr_min_text_len
                res_text = ocr_text if is_valid else "Nicht lesbar"
                res_conf = 1.0 if is_valid else 0.0
            else:
                res_text, res_conf = "Nicht lesbar", 0.0
            
            for eintrag in text_eintraege:
                eintrag.update({
                    "stufe_3_aktiv": True, 
                    "box_padded": padded_box, 
                    "ergebnis": res_text, 
                    "ocr_conf": res_conf
                })
                funde.append(eintrag)

        dauer = round((time.time() - start_ms) * 1000, 2)

        prediction_response = {
            "metadata": {
                "datei": os.path.basename(bild_pfad), 
                "gesamtzeit_ms": float(dauer), 
                "s1_konfidenz": round(conf_s1, 4)
            },
            "ergebnisse": funde
        }

        return {
            "confidence": round(conf_s1, 4),
            "ocrText": res_text,
            "ocrConfidence": round(float(res_conf), 4),
            "durationMs": dauer,
            "detections": prediction_response, 
        }