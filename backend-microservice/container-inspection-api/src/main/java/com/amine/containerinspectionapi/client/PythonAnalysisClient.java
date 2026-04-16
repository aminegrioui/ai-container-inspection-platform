package com.amine.containerinspectionapi.client;


import com.amine.containerinspectionapi.dtos.PythonAnalysisResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Component
public class PythonAnalysisClient {

    private final RestTemplate restTemplate;
    private final String pythonBaseUrl;

    public PythonAnalysisClient(
            RestTemplate restTemplate,
            @Value("${python.service.url}") String pythonBaseUrl) {
        this.restTemplate = restTemplate;
        this.pythonBaseUrl = pythonBaseUrl;
    }

    /**
     * Sends the image filename to Python and returns the analysis result.
     *
     * @param imageName  the plain filename (e.g. "abc123.jpg"), NOT the full path
     */
    public PythonAnalysisResponseDto analyze(String imageName) {
        String url = pythonBaseUrl + "/analyze";
        Map<String, String> body = Map.of("image_name", imageName);

        return restTemplate.postForObject(url, body, PythonAnalysisResponseDto.class);
    }
}