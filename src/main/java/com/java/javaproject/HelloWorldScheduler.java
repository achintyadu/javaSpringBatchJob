package com.java.javaproject;

import com.java.javaproject.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldScheduler {

    @Autowired
    private ApiService apiService;

    @Scheduled(fixedRate = 5000)
    public void fetchDataFromApi() {
        System.out.println("\n[" + java.time.LocalDateTime.now() + "] Scheduler Running...");
        try {
            apiService.fetchAndSaveData();
        } catch (Exception e) {
            System.err.println("Scheduler failed: " + e.getMessage());
        }
    }
}
