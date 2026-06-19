package com.taskflow_backend.controller;

import com.taskflow_backend.service.SqsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.model.Message;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sqs")
@RequiredArgsConstructor
public class SqsController {

    private final SqsService sqsService;

    // Message bhejo
    @PostMapping("/send")
    public ResponseEntity<?> send(
            @RequestBody Map<String, 
            String> request) {

        String messageId = sqsService
            .sendMessage(request.get("message"));

        return ResponseEntity.ok(Map.of(
            "messageId", messageId,
            "message", "Message sent to SQS!"
        ));
    }

    // Messages receive karo
    @GetMapping("/receive")
    public ResponseEntity<?> receive() {
        List<Message> messages = 
            sqsService.receiveMessages();

        return ResponseEntity.ok(Map.of(
            "count", messages.size(),
            "messages", messages.stream()
                .map(m -> Map.of(
                    "messageId", m.messageId(),
                    "body", m.body(),
                    "receiptHandle", 
                        m.receiptHandle()
                ))
                .toList()
        ));
    }

    // Message delete karo
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestBody Map<String, 
            String> request) {

        sqsService.deleteMessage(
            request.get("receiptHandle"));

        return ResponseEntity.ok(Map.of(
            "message", "Message deleted!"
        ));
    }
}