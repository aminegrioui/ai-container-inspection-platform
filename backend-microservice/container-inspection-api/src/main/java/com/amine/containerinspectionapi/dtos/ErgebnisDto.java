package com.amine.containerinspectionapi.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ErgebnisDto {


    private String klasse;
    private Double yolo_conf;
    private List<Integer> box;

    private Boolean stufe_3_aktiv;

    private List<Integer> box_padded;
    private String ergebnis;

    private Double ocr_conf;
}
