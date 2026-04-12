# 🚢 Model Microservice — Container Inspection Pipeline

> **Part of a full-stack microservices application** built on top of a Deep Learning pipeline developed during a Master's module in collaboration with fellow students.

---

## 📖 Introduction

Manual visual inspection of shipping containers in port operations is a well-known bottleneck. Hazardous material labels must be correctly identified, container IDs reliably read, and throughput kept as high as possible — all under challenging real-world conditions such as variable lighting, weather, oblique camera angles, and surface contamination.

This microservice exposes a **three-stage Deep Learning cascade pipeline** via a REST API:

| Stage | Task | Purpose |
|-------|------|---------|
| **Stage 1** | Image Classification | Discard irrelevant images early (*Early Exit*) to reduce latency and cost |
| **Stage 2** | Object Detection (YOLO) | Localise hazardous-material labels, text fields, and containers via bounding boxes |
| **Stage 3** | OCR | Extract container IDs from the crops produced by Stage 2 |

Each stage has a single, well-defined responsibility and passes a structured result to the next stage — a modular design that makes the pipeline easy to extend and integrate into an industrial environment.

---

## 🗂 Project Structure

```
model_microservice/
├── app/
│   ├── ...          # Application source code
│   └── test/        # Sample images for local testing
└── requirements.txt
```

---

## 🚀 Getting Started

### 1. Install dependencies

```bash
pip install -r requirements.txt
```

### 2. Start the service

```bash
python app/main.py
```

The API will be available at `http://127.0.0.1:5000`.

---

## 🔌 API Reference

### `POST /api/v1/analyze`

Runs the full three-stage pipeline on a single container image.

**Request**

```http
POST http://127.0.0.1:5000/api/v1/analyze
Content-Type: application/json
```

```json
{
  "image_name": "1645794076_Camera_Cr.jpg"
}
```

> 📁 Place test images in `app/test/`. The `image_name` field refers to a file inside that directory.

**Response**

```json
{
  "status": "success",
  "stage_1": {
    "is_container": true,
    "confidence": 0.97
  },
  "stage_2": {
    "detections": [
      {
        "label": "hazmat_label",
        "confidence": 0.91,
        "bbox": [120, 45, 230, 160]
      },
      {
        "label": "text_field",
        "confidence": 0.88,
        "bbox": [300, 20, 520, 80]
      }
    ]
  },
  "stage_3": {
    "container_id": "MSCU1234567"
  }
}
```

> If Stage 1 classifies the image as **not a container**, the pipeline exits early and stages 2 & 3 are skipped.

---

## 🧪 Quick Test with `curl`

```bash
curl -X POST http://127.0.0.1:5000/api/v1/analyze \
     -H "Content-Type: application/json" \
     -d '{"image_name": "1645794076_Camera_Cr.jpg"}'
```

---

## 🛠 Tech Stack

- **Python** — core runtime
- **Flask** — REST API layer
- **Ultralytics YOLO** — object detection (Stage 2)
- **OCR engine** — text extraction (Stage 3)
- **PyTorch** — model inference backend

---

## 📌 Notes

- This is the **model microservice** — the first service in a larger planned microservices architecture.
- It is responsible for direct interaction with the ML models and is designed to be consumed by an orchestrating backend service.
- The pipeline was originally developed as part of a **Master's Deep Learning module** and is being extended here into a production-ready service.