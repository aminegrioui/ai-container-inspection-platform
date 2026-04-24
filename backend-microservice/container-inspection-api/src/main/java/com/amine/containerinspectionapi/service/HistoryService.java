package com.amine.containerinspectionapi.service;


import com.amine.containerinspectionapi.dtos.HistoryItemResponse;
import com.amine.containerinspectionapi.dtos.PageResponse;
import com.amine.containerinspectionapi.model.PredictionStatus;
import com.amine.containerinspectionapi.repository.PredictionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private static final int MAX_PAGE_SIZE = 50;

    private final PredictionRepository repository;

    public HistoryService(PredictionRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns paginated history.
     *
     * @param page    page number (0-based)
     * @param size    items per page (max 50)
     * @param status  optional filter — null means all
     * @param ocrText optional search by container number
     */
    public PageResponse<HistoryItemResponse> getHistory(
            int page,
            int size,
            PredictionStatus status,
            String ocrText
    ) {
        // guard: never return more than MAX_PAGE_SIZE
        int safeSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, safeSize);

        if (ocrText != null && !ocrText.isBlank()) {
            return PageResponse.from(
                    repository.findByOcrTextContainingIgnoreCase(ocrText, pageable)
                            .map(HistoryItemResponse::from)
            );
        }

        if (status != null) {
            return PageResponse.from(
                    repository.findByStatusOrderByCreatedAtDesc(status, pageable)
                            .map(HistoryItemResponse::from)
            );
        }

        return PageResponse.from(
                repository.findAllByOrderByCreatedAtDesc(pageable)
                        .map(HistoryItemResponse::from)
        );
    }
}