package com.java.javaproject.repository;

import com.java.javaproject.model.ApiData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDataRepository extends MongoRepository<ApiData, String> {
}
