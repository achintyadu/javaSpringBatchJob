package com.java.javaproject.repository;

import com.java.javaproject.model.ApiData;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiDataRepository extends MongoRepository<ApiData, Integer> {
    void bulkWrite(List<WriteModel<Document>> operations);
}
