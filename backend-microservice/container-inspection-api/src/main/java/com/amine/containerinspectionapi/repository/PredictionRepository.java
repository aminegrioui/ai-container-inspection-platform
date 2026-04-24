package com.amine.containerinspectionapi.repository;


import com.amine.containerinspectionapi.model.Prediction;
import com.amine.containerinspectionapi.model.PredictionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    Optional<Prediction> findPredictionByImageName(String imageName);

    // --- history: all, newest first ---
    Page<Prediction> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // --- filter by status (PROCESSING / COMPLETED / ERROR) ---
    Page<Prediction> findByStatusOrderByCreatedAtDesc(PredictionStatus status, Pageable pageable);

    // --- search by OCR text (container number) ---
    @Query("""
        SELECT p FROM Prediction p
        WHERE LOWER(p.ocrText) LIKE LOWER(CONCAT('%', :ocrText, '%'))
        ORDER BY p.createdAt DESC
    """)
    Page<Prediction> findByOcrTextContainingIgnoreCase(
            @Param("ocrText") String ocrText,
            Pageable pageable
    );
}