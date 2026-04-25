package com.amine.containerinspectionapi.controller;



import com.amine.containerinspectionapi.dtos.HistoryItemResponse;
import com.amine.containerinspectionapi.dtos.PageResponse;
import com.amine.containerinspectionapi.dtos.PredictionResponseDto;
import com.amine.containerinspectionapi.model.PredictionStatus;
import com.amine.containerinspectionapi.service.HistoryService;
import com.amine.containerinspectionapi.service.PredictionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class PredictionController {

    private final PredictionService predictionService;
    private final HistoryService historyService;

    public PredictionController(PredictionService predictionService, HistoryService historyService) {
        this.predictionService = predictionService;
        this.historyService = historyService;
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PredictionResponseDto> upload(
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        PredictionResponseDto response = predictionService.uploadAndAnalyze(file);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/history")
    public ResponseEntity<PageResponse<HistoryItemResponse>> getHistory(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PredictionStatus status,
            @RequestParam(required = false)    String ocrText
    ) {
        PageResponse<HistoryItemResponse> result =
                historyService.getHistory(page, size, status, ocrText);
        return ResponseEntity.ok(result);
    }
}