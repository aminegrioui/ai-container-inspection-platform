package com.amine.containerinspectionapi.dtos;




import lombok.Data;
import java.time.OffsetDateTime;



@Data
public class PredictionResponseDto {

    private Long id;
    private String imageName;
    private String status;
    private String result;
    private Double confidence;
    private String ocrText;
    private Double durationMs;
    private DetectionsDto detections;
    private OffsetDateTime createdAt;
    private OffsetDateTime respondedAt;
    private boolean isNewAnalyse;

}
