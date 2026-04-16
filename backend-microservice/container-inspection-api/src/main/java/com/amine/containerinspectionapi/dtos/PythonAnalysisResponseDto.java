package com.amine.containerinspectionapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PythonAnalysisResponseDto {

    private Double confidence;
    private String ocrText;
    private Double durationMs;
    private DetectionsDto detections;
}
