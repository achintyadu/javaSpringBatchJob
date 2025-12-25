package com.java.javaproject.service;

import com.java.javaproject.model.ApiData;
import com.java.javaproject.repository.ApiDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiService {

    @Autowired
    private ApiDataRepository apiDataRepository;

    private final WebClient webClient;

    public ApiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    public void fetchAndSaveData() {
        try {
            ApiData[] apiDataArray = webClient.get()
                    .uri("/posts")
                    .retrieve()
                    .bodyToMono(ApiData[].class)
                    .block();

            if (apiDataArray != null && apiDataArray.length > 0) {
                int totalItems = apiDataArray.length;
                int savedCount = 0;
                
                for (ApiData apiData : apiDataArray) {
                    try {
                        apiDataRepository.save(apiData);
                        savedCount++;
                    } catch (Exception e) {
                        System.err.println("Failed to save item: " + e.getMessage());
                    }
                }
                
                System.out.println("API Items: " + totalItems + " | Saved to DB: " + savedCount);
            } else {
                System.out.println("No data received from API");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
