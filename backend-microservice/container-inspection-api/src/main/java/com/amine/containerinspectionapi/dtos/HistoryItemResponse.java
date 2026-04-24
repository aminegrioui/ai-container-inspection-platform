package com.amine.containerinspectionapi.dtos;

import com.amine.containerinspectionapi.model.Prediction;
import com.amine.containerinspectionapi.model.PredictionStatus;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Lean DTO for the history list.
 * Detections (heavy JSONB) are excluded — load them only on detail view.
 */
@Data
public class HistoryItemResponse {

    private Long          id;
    private String        imageName;
    private PredictionStatus status;
    private String        result;
    private Double    confidence;
    private String        ocrText;
    private Double       durationMs;
    private OffsetDateTime createdAt;
    private OffsetDateTime respondedAt;

    // ---- static factory from entity ----
    public static HistoryItemResponse from(Prediction p) {
        HistoryItemResponse dto = new HistoryItemResponse();
        dto.id            = p.getId();
        dto.imageName     = p.getImageName();
        dto.status        = p.getStatus();
        dto.result        = p.getResult();
        dto.confidence    = p.getConfidence();
        dto.ocrText       = p.getOcrText();
        dto.durationMs    = p.getDurationMs();
        dto.createdAt     = p.getCreatedAt();
        dto.respondedAt   = p.getRespondedAt();
        return dto;
    }

}