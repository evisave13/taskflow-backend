package com.taskflow_backend.controller;

import com.taskflow_backend.service.DynamoDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dynamo")
@RequiredArgsConstructor
public class DynamoDbController {

    private final DynamoDbService dynamoDbService;

    // CREATE
    @PostMapping("/students")
    public ResponseEntity<?> save(
            @RequestBody Map<String, 
            Object> request) {

        dynamoDbService.saveStudent(
            (String) request.get("studentId"),
            (String) request.get("name"),
            (String) request.get("city"),
            (Integer) request.get("age")
        );

        return ResponseEntity.ok(Map.of(
            "message", "Student saved!"
        ));
    }

    // READ
    @GetMapping("/students/{id}")
    public ResponseEntity<?> get(
            @PathVariable String id) {
        return ResponseEntity.ok(
            dynamoDbService.getStudent(id));
    }

    // UPDATE
    @PutMapping("/students/{id}")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @RequestBody Map<String, 
            String> request) {

        dynamoDbService.updateStudent(
            id, request.get("city"));

        return ResponseEntity.ok(Map.of(
            "message", "Student updated!"
        ));
    }

    // DELETE
    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id) {

        dynamoDbService.deleteStudent(id);
        return ResponseEntity.ok(Map.of(
            "message", "Student deleted!"
        ));
    }

    // GET ALL
    @GetMapping("/students")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
            dynamoDbService.getAllStudents());
    }
}