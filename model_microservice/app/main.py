import uvicorn
from fastapi import FastAPI
from app.api.v1.controller import router as analyze_router

app = FastAPI(title="Container AI Service")


app.include_router(analyze_router, prefix="/api/v1", tags=["Analysis"])

# if __name__ == "__main__":
#     uvicorn.run("app.main:app", host="0.0.0.0", port=5000, reload=True)