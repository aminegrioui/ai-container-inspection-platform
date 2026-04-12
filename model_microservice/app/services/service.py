import os
from fastapi import HTTPException
from app.core.config import settings
from app.services.modelcaller import ContainerIntelligenceCore

class AnalyseService:
    def __init__(self):
    
        print(f"--- Initialisiere {settings.APP_NAME} Pipeline ---")
        self.pipeline = ContainerIntelligenceCore(
            s1_path=settings.MODEL_PATH_S1,
            s2_path=settings.MODEL_PATH_S2,
            trocr_path=settings.MODEL_PATH_OCR
        )

    def run_analysis(self, img_name: str):
        
        img_path=settings.PATH_TO_IMAGE_DIR + img_name
        if not os.path.exists(img_path):
            raise HTTPException(
                status_code=400, 
                detail=f"Bild nicht gefunden unter: {img_path}"
            )
        
        try:
        
            result = self.pipeline.prozess(img_path)
            return result
        except Exception as e:
        
            print(f"Fehler während der Analyse: {e}")
            raise HTTPException(status_code=500, detail="Interner Modell-Fehler")

analyse_service_instance = None

def get_analyse_service():
    global analyse_service_instance
    if analyse_service_instance is None:
        analyse_service_instance = AnalyseService()
    return analyse_service_instance