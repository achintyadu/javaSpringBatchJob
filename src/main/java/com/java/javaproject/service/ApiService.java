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
                ApiData apiData = apiDataArray[0];
                System.out.println("=====================================");
                System.out.println("API Data Received:");
                System.out.println("Title: " + apiData.getTitle());
                System.out.println("User ID: " + apiData.getUserId());
                System.out.println("Body: " + apiData.getBody());
                System.out.println("=====================================");
                
                // Save to MongoDB
                ApiData savedData = apiDataRepository.save(apiData);
                System.out.println("Data saved to MongoDB with ID: " + savedData.getId());
            }
        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }
}
