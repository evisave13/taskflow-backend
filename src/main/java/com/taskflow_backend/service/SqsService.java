package com.taskflow_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;

@Service
@Slf4j
public class SqsService {

    private final SqsClient sqsClient;
    private final String QUEUE_URL = 
        "https://sqs.ap-south-1.amazonaws.com/" +
        "980921727144/taskflow-task-queue";

    public SqsService() {
        this.sqsClient = SqsClient.builder()
            .region(Region.AP_SOUTH_1)
            .build();
    }

    // Message bhejo (Producer)
    public String sendMessage(String message) {
        SendMessageRequest request = 
            SendMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .messageBody(message)
                .build();

        SendMessageResponse response = 
            sqsClient.sendMessage(request);

        log.info("Message sent! ID: {}", 
            response.messageId());
            
        return response.messageId();
    }

    // Message receive karo (Consumer)
    public List<Message> receiveMessages() {
        ReceiveMessageRequest request = 
            ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(5)
                .build();

        List<Message> messages = sqsClient
            .receiveMessage(request)
            .messages();

        log.info("Received {} messages", 
            messages.size());
            
        return messages;
    }

    // Message delete karo (after processing)
    public void deleteMessage(
            String receiptHandle) {
        DeleteMessageRequest request = 
            DeleteMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(request);
        log.info("Message deleted!");
    }
}