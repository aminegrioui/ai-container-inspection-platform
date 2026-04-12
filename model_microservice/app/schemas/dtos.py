
from pydantic import BaseModel
from typing import Optional, List, Dict
class AnalysisRequest(BaseModel):
    image_name: str

class AnalysisResponse(BaseModel):
    confidence: float
    ocrText: Optional[str]
    durationMs: float
    detections: dict