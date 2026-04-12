from .base import BaseAppSettings

class TestSettings(BaseAppSettings):
    MODEL_PATH_S1: str = "./runs/weights/classification/efficientnet_final.pth"
    MODEL_PATH_S2: str = "./runs/weights/detect/best.pt"
    MODEL_PATH_OCR: str = "./runs/weights/ocr/trocr-container-best-model"
    PATH_TO_IMAGE_DIR: str = "app/test/"