from pydantic_settings import BaseSettings

class BaseAppSettings(BaseSettings):
    APP_NAME: str = "Container-AI-Service"
    MODEL_PATH_S1: str = ""
    MODEL_PATH_S2: str = ""
    MODEL_PATH_OCR: str = ""
    PATH_TO_IMAGE_DIR: str = ""
    API_PORT: int = 5000