package com.taskflow_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;
    private final String BUCKET = 
        "taskflow-files-ekant"; // tumhara bucket name

    // EC2 pe IAM Role se 
    // credentials automatic milenge!
    public S3Service() {
        this.s3Client = S3Client.builder()
            .region(Region.AP_SOUTH_1)
            .build();
    }

    // File Upload karo
    public String uploadFile(
            MultipartFile file) throws IOException {

        // Unique file name banao
        String fileName = UUID.randomUUID()
            + "_" + file.getOriginalFilename();

        // S3 pe upload karo
        PutObjectRequest request = 
            PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(fileName)
                .contentType(
                    file.getContentType())
                .build();

        s3Client.putObject(request,
            RequestBody.fromBytes(
                file.getBytes()));

        // File URL return karo
        String fileUrl = "https://" + BUCKET 
            + ".s3.ap-south-1.amazonaws.com/" 
            + fileName;

        log.info("File uploaded: {}", fileUrl);
        return fileUrl;
    }

    // File Delete karo
    public void deleteFile(String fileName) {
        DeleteObjectRequest request = 
            DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(fileName)
                .build();

        s3Client.deleteObject(request);
        log.info("File deleted: {}", fileName);
    }

    // List all files
    public ListObjectsV2Response listFiles() {
        ListObjectsV2Request request = 
            ListObjectsV2Request.builder()
                .bucket(BUCKET)
                .build();

        return s3Client.listObjectsV2(request);
    }
}