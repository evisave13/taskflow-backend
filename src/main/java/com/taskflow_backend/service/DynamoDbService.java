package com.taskflow_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.util.*;

@Service
@Slf4j
public class DynamoDbService {

    private final DynamoDbClient client;
    private final String TABLE = "students";

    public DynamoDbService() {
        this.client = DynamoDbClient.builder()
            .region(Region.AP_SOUTH_1)
            .build();
    }

    // CREATE — Student save karo
    public void saveStudent(
            String studentId,
            String name,
            String city,
            int age) {

        Map<String, AttributeValue> item =
            new HashMap<>();

        item.put("studentId",
            AttributeValue.fromS(studentId));
        item.put("name",
            AttributeValue.fromS(name));
        item.put("city",
            AttributeValue.fromS(city));
        item.put("age",
            AttributeValue.fromN(
                String.valueOf(age)));

        PutItemRequest request =
            PutItemRequest.builder()
                .tableName(TABLE)
                .item(item)
                .build();

        client.putItem(request);
        log.info("Student saved: {}", studentId);
    }

    // READ — Student fetch karo
    public Map<String, String> getStudent(
            String studentId) {

        Map<String, AttributeValue> key =
            new HashMap<>();
        key.put("studentId",
            AttributeValue.fromS(studentId));

        GetItemRequest request =
            GetItemRequest.builder()
                .tableName(TABLE)
                .key(key)
                .build();

        Map<String, AttributeValue> item =
            client.getItem(request).item();

        if (item.isEmpty()) {
            return Map.of("error",
                "Student not found!");
        }

        // Convert to readable format
        Map<String, String> result =
            new HashMap<>();
        item.forEach((k, v) ->
            result.put(k,
                v.s() != null ? v.s() :
                v.n() != null ? v.n() : ""));

        return result;
    }

    // UPDATE — Student update karo
    public void updateStudent(
            String studentId,
            String city) {

        Map<String, AttributeValue> key =
            new HashMap<>();
        key.put("studentId",
            AttributeValue.fromS(studentId));

        Map<String, AttributeValueUpdate> updates =
            new HashMap<>();
        updates.put("city",
            AttributeValueUpdate.builder()
                .value(AttributeValue.fromS(city))
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request =
            UpdateItemRequest.builder()
                .tableName(TABLE)
                .key(key)
                .attributeUpdates(updates)
                .build();

        client.updateItem(request);
        log.info("Student updated: {}", studentId);
    }

    // DELETE — Student delete karo
    public void deleteStudent(String studentId) {

        Map<String, AttributeValue> key =
            new HashMap<>();
        key.put("studentId",
            AttributeValue.fromS(studentId));

        DeleteItemRequest request =
            DeleteItemRequest.builder()
                .tableName(TABLE)
                .key(key)
                .build();

        client.deleteItem(request);
        log.info("Student deleted: {}", studentId);
    }

    // SCAN — Sab students fetch karo
    public List<Map<String, String>> 
            getAllStudents() {

        ScanRequest request =
            ScanRequest.builder()
                .tableName(TABLE)
                .build();

        List<Map<String, String>> results =
            new ArrayList<>();

        client.scan(request).items()
            .forEach(item -> {
                Map<String, String> student =
                    new HashMap<>();
                item.forEach((k, v) ->
                    student.put(k,
                        v.s() != null ? v.s() :
                        v.n() != null ? v.n() : ""));
                results.add(student);
            });

        return results;
    }
}