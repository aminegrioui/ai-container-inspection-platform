package com.amine.containerinspectionapi.dtos;

import lombok.Data;

@Data
public class MetadataDto {
    private String datei;
    private Double gesamtzeit_ms;
    private Double s1_konfidenz;
}
