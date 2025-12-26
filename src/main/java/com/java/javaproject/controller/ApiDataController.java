package com.java.javaproject.controller;

import com.java.javaproject.model.ApiData;
import com.java.javaproject.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/data")
public class ApiDataController {

    @Autowired
    private ApiService apiService;

    @PostMapping
    public ResponseEntity<ApiData> createData(@RequestBody ApiData apiData) {
        try {
            ApiData saved = apiService.saveData(apiData);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>((ApiData) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ApiData>> getAllData() {
        try {
            List<ApiData> dataList = apiService.getAllData();
            if (dataList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(dataList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<ApiData>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiData> getDataById(@PathVariable("id") String id) {
        try {
            Optional<ApiData> data = apiService.getDataById(Integer.parseInt(id));
            return data.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>((ApiData) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiData> updateData(@PathVariable("id") String id, @RequestBody ApiData apiData) {
        try {
            Optional<ApiData> updated = apiService.updateData(Integer.parseInt(id), apiData);
            return updated.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>((ApiData) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteData(@PathVariable("id") String id) {
        try {
            boolean deleted = apiService.deleteData(Integer.parseInt(id));
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllData() {
        try {
            apiService.deleteAllData();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
