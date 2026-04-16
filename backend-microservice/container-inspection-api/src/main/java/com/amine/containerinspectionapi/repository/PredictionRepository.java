package com.amine.containerinspectionapi.repository;


import com.amine.containerinspectionapi.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    Optional<Prediction> findPredictionByImageName(String imageName);
}