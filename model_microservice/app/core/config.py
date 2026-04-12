import os
from app.core.settings.local import LocalSettings
from app.core.settings.test import TestSettings
from app.core.settings.prod import ProdSettings

def get_settings():
    env = os.getenv("ENV", "local").lower()
    if env == "prod":
        return ProdSettings()
    elif env == "test":
        return TestSettings()
    return LocalSettings()

settings = get_settings()