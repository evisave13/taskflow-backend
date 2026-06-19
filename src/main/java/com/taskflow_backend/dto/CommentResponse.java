package com.taskflow_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private Long projectId;
    private LocalDateTime createdAt;
}
