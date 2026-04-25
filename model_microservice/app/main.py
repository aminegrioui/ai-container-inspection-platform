import uvicorn
from fastapi import FastAPI
from app.api.v1.controller import router as analyze_router

app = FastAPI(title="Container AI Service")


app.include_router(analyze_router, prefix="/api/v1", tags=["Analysis"])