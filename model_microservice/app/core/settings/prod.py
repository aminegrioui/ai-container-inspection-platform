from .base import BaseAppSettings

class ProdSettings(BaseAppSettings):
    MODEL_PATH_S1: str = "./models/classification/efficientnet_final.pth"
    MODEL_PATH_S2: str = "./models/detect/best.pt"
    MODEL_PATH_OCR: str = "./models/ocr/trocr-container-best-model"
    PATH_TO_IMAGE_DIR: str = "/data/images/"