package com.example.testtaskoveronix.service.downloader;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Component
public class HttpClient {

    public <T> T get(String url, Class<? extends T> clazz, String apiKey) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiKey != null)
            headers.add("apiKey", apiKey);

        ResponseEntity<? extends T> response = null;
        try {
            HttpEntity<T> entity = new HttpEntity<>(null, headers);
            response = restTemplate.exchange(url, HttpMethod.GET, entity, clazz);
        } catch (Exception e) {
            log.error("Exception in HttpClient: " + e.getMessage());
        }
        return response != null ? response.getBody() : null;
    }
}
