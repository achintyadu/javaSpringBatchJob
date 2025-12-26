package com.java.javaproject.service;

import com.java.javaproject.model.ApiData;
import com.java.javaproject.repository.ApiDataRepository;
import com.java.javaproject.repository.ApiDataRepositoryImpl;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    @Autowired
    private ApiDataRepository apiDataRepository;

    @Autowired
    private ApiDataRepositoryImpl apiDataRepositoryImpl;

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
                
                // Build bulk operations
                List<WriteModel<Document>> operations = buildBulkOperations(apiDataArray);
                
                // Execute bulk write
                if (!operations.isEmpty()) {
                    apiDataRepositoryImpl.bulkWrite(operations);
                    System.out.println("API Items: " + totalItems + " | Bulk operations executed: " + operations.size());
                }
            } else {
                System.out.println("No data received from API");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<WriteModel<Document>> buildBulkOperations(ApiData[] apiDataArray) {
        List<WriteModel<Document>> operations = new ArrayList<>();
        
        for (ApiData apiData : apiDataArray) {
            if (apiData == null || apiData.getId() == null) {
                continue;
            }
            
            // Create filter based on _id (MongoDB's primary key)
            Document filter = new Document("_id", apiData.getId());
            
            // Create document with all fields including _id
            Document document = new Document()
                    .append("_id", apiData.getId())
                    .append("title", apiData.getTitle())
                    .append("userId", apiData.getUserId())
                    .append("body", apiData.getBody())
                    .append("timestamp", LocalDateTime.now())
                    .append("lastUpdated", formatDate12Hour())
                    .append("ttlAt", LocalDateTime.now().plusDays(10));
            
            // Add update operation with upsert
            operations.add(new UpdateOneModel<>(
                    filter,
                    new Document("$set", document),
                    new UpdateOptions().upsert(true)
            ));
        }
        
        return operations;
    }

    private String formatDate12Hour() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        return LocalDateTime.now().format(formatter);
    }

    // CRUD Operations
    public ApiData saveData(ApiData apiData) {
        return apiDataRepository.save(apiData);
    }

    public List<ApiData> getAllData() {
        return apiDataRepository.findAll();
    }

    public Optional<ApiData> getDataById(Integer id) {
        return apiDataRepository.findById(id);
    }

    public Optional<ApiData> updateData(Integer id, ApiData newData) {
        return apiDataRepository.findById(id).map(existingData -> {
            existingData.setTitle(newData.getTitle());
            existingData.setUserId(newData.getUserId());
            existingData.setBody(newData.getBody());
            existingData.setTimestamp(LocalDateTime.now());
            return apiDataRepository.save(existingData);
        });
    }

    public boolean deleteData(Integer id) {
        if (apiDataRepository.existsById(id)) {
            apiDataRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllData() {
        apiDataRepository.deleteAll();
    }
}
