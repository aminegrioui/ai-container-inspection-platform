package com.amine.containerinspectionapi.service;


import com.amine.containerinspectionapi.client.PythonAnalysisClient;


import com.amine.containerinspectionapi.dtos.PredictionResponseDto;
import com.amine.containerinspectionapi.dtos.PythonAnalysisResponseDto;
import com.amine.containerinspectionapi.model.Prediction;
import com.amine.containerinspectionapi.model.PredictionStatus;
import com.amine.containerinspectionapi.repository.PredictionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PredictionService {

    private static final Logger log = LoggerFactory.getLogger(PredictionService.class);
    private static final Double THRESHOLD = 0.5;

    private final PredictionRepository repository;
    private final PythonAnalysisClient pythonClient;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public PredictionService(
            PredictionRepository repository,
            PythonAnalysisClient pythonClient) {

        this.repository = repository;
        this.pythonClient = pythonClient;
    }

    /**
     * Full pipeline:
     * 1. Persist the incoming file to the shared upload directory.
     * 2. Create a PROCESSING prediction record in the DB.
     * 3. Call the Python microservice with just the filename.
     * 4. Map the response → entity fields, mark COMPLETED.
     * 5. Return a flat DTO to the controller.
     */
    public PredictionResponseDto uploadAndAnalyze(MultipartFile file) throws IOException {

        // 1 – Save file to the known shared directory
        String originalFilename = file.getOriginalFilename();
        String uniqueName = UUID.randomUUID() + "_" + originalFilename;
        Path target = Paths.get(uploadDir).resolve(originalFilename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        log.info("Saved uploaded file to {}", target);

        // 2 – Persist initial PROCESSING record
        Prediction prediction = new Prediction();
        Optional<Prediction> predictionOptional = repository.findPredictionByImageName(originalFilename);
        if (predictionOptional.isPresent()) {
            prediction = predictionOptional.get();
        }
        else {
            prediction.setImageName(originalFilename);
            prediction.setStatus(PredictionStatus.PROCESSING);
            prediction = repository.save(prediction);

            // 3 – Call Python (sends only the filename, Python resolves it against its own app.upload.dir)
            PythonAnalysisResponseDto pythonResponse;
            try {
                pythonResponse = pythonClient.analyze(originalFilename);
            } catch (Exception ex) {
                log.error("Python service call failed for {}", uniqueName, ex);
                prediction.setStatus(PredictionStatus.FAILED);
                repository.save(prediction);
                throw new RuntimeException("Analysis service unavailable: " + ex.getMessage(), ex);
            }

            // 4 – Map response → entity
            try{
                mapPythonResponseToEntity(prediction, pythonResponse);
                prediction = repository.save(prediction);
            }catch (Exception ex){
                repository.delete(prediction);
                throw new RuntimeException("Analysis service unavailable: " + ex.getMessage(), ex);
            }
        }

        // 5 – Return DTO
        return toResponseDto(prediction);
    }

    // ── Mapping helpers ───────────────────────────────────────────────────

    private void mapPythonResponseToEntity(Prediction prediction,
                                           PythonAnalysisResponseDto resp) {

        prediction.setConfidence(resp.getConfidence());
        prediction.setOcrText(resp.getOcrText());
        prediction.setDurationMs(resp.getDurationMs() != null ? resp.getDurationMs() : null);

        // Derive result label from confidence threshold
        prediction.setResult(resp.getConfidence().compareTo(THRESHOLD) > 0 ? "Positiv" : "Negativ");

        // Serialize detections map → JSONB string


        if (resp.getDetections() != null) {
            prediction.setDetections(resp.getDetections());
        }

        prediction.setStatus(PredictionStatus.COMPLETED);
        prediction.setRespondedAt(OffsetDateTime.now());
    }

    private PredictionResponseDto toResponseDto(Prediction p) {
        PredictionResponseDto dto = new PredictionResponseDto();
        dto.setId(p.getId());
        dto.setImageName(p.getImageName());
        dto.setStatus(p.getStatus().name());
        dto.setResult(p.getResult());
        dto.setConfidence(p.getConfidence());
        dto.setOcrText(p.getOcrText());
        dto.setDurationMs(p.getDurationMs());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setRespondedAt(p.getRespondedAt());

        // Parse stored JSONB string back to a JsonNode so Angular gets a real object

        if (p.getDetections() != null) {
            try {
                dto.setDetections(p.getDetections());
            } catch (Exception e) {
                throw new RuntimeException("Detection service unavailable: " + e.getMessage(), e);
            }
        }

        return dto;
    }
}