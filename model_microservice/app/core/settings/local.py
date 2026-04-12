from .base import BaseAppSettings

class LocalSettings(BaseAppSettings):
    MODEL_PATH_S1: str = "app/core/runs/weights/classification/efficientnet_final.pth"
    MODEL_PATH_S2: str = "app/core/runs/weights/detect/best.pt"
    MODEL_PATH_OCR: str = "app/core/runs/weights/ocr/trocr-container-best-model"
    PATH_TO_IMAGE_DIR: str = "app/test/"