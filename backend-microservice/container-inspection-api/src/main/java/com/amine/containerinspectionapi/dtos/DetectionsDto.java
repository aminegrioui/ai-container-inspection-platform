package com.amine.containerinspectionapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionsDto {
    private MetadataDto metadata;
    private List<ErgebnisDto> ergebnisse;
}
