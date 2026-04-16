package com.amine.containerinspectionapi.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class PredictionHistoryDto {

    private Long id;
    private String imageName;
    private String status;
    private String result;
    private BigDecimal confidence;
    private String ocrText;
    private Integer durationMs;
    private OffsetDateTime createdAt;
    private OffsetDateTime respondedAt;

    public PredictionHistoryDto() {}

    // JPQL constructor expression – matches the query in the repository
    public PredictionHistoryDto(Long id, String imageName, String status,
                                String result, BigDecimal confidence,
                                String ocrText, Integer durationMs,
                                OffsetDateTime createdAt, OffsetDateTime respondedAt) {
        this.id = id;
        this.imageName = imageName;
        this.status = status;
        this.result = result;
        this.confidence = confidence;
        this.ocrText = ocrText;
        this.durationMs = durationMs;
        this.createdAt = createdAt;
        this.respondedAt = respondedAt;
    }

}
