package com.amine.containerinspectionapi.model;


import com.amine.containerinspectionapi.dtos.DetectionsDto;
import jakarta.persistence.*;
import lombok.Data;


import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "prediction")
@Data
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PredictionStatus status = PredictionStatus.PROCESSING;

    @Column(name = "result")
    private String result;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "ocr_text")
    private String ocrText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private DetectionsDto detections;


    @Column(name = "duration_ms")
    private Double durationMs;


    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "responded_at")
    private OffsetDateTime respondedAt;
}