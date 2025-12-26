package com.java.javaproject.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiDataRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void bulkWrite(List<WriteModel<Document>> operations) {
        if (operations == null || operations.isEmpty()) {
            System.out.println("No operations to execute");
            return;
        }

        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection("javaTest");
            
            BulkWriteOptions options = new BulkWriteOptions().ordered(false);
            var result = collection.bulkWrite(operations, options);
            
            System.out.println("Bulk Write Results:");
            System.out.println("  - Inserted: " + result.getInsertedCount());
            System.out.println("  - Modified: " + result.getModifiedCount());
            System.out.println("  - Upserted: " + result.getUpserts().size());
            
        } catch (Exception e) {
            System.err.println("Bulk write failed: " + e.getMessage());
            throw e;
        }
    }
}
