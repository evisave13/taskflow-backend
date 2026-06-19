package com.taskflow_backend.controller;

import com.taskflow_backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    // File Upload API
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") 
            MultipartFile file) {
        try {
            String url = 
                s3Service.uploadFile(file);

            return ResponseEntity.ok(
                Map.of(
                    "message", "File uploaded!",
                    "url", url,
                    "fileName", 
                        file.getOriginalFilename(),
                    "size", file.getSize()
                )
            );
        } catch (IOException e) {
            return ResponseEntity
                .internalServerError()
                .body(Map.of(
                    "error", e.getMessage()));
        }
    }

    // List Files API
    @GetMapping
    public ResponseEntity<?> listFiles() {
        var files = s3Service.listFiles();
        return ResponseEntity.ok(
            Map.of(
                "files", files.contents()
                    .stream()
                    .map(f -> Map.of(
                        "key", f.key(),
                        "size", f.size()
                    ))
                    .toList()
            )
        );
    }

    // Delete File API
    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> delete(
            @PathVariable String fileName) {
        s3Service.deleteFile(fileName);
        return ResponseEntity.ok(
            Map.of("message", "File deleted!"));
    }
}