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
        System.out.println("Fetching data from API...");
        apiService.fetchAndSaveData();
    }
}
