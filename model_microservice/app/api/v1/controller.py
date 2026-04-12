from fastapi import APIRouter, Depends
from app.schemas.dtos import AnalysisRequest, AnalysisResponse
from app.services.service import get_analyse_service, AnalyseService

router = APIRouter()

@router.post("/analyze", response_model=AnalysisResponse)
def analyze(
    request: AnalysisRequest, 
    service: AnalyseService = Depends(get_analyse_service)
):

    return service.run_analysis(request.image_name)