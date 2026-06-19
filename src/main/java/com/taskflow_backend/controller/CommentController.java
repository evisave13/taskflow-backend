package com.taskflow_backend.controller;

import com.taskflow_backend.dto.ApiResponse;
import com.taskflow_backend.dto.CommentRequest;
import com.taskflow_backend.dto.CommentResponse;
import com.taskflow_backend.entity.User;
import com.taskflow_backend.service.CommentService;
import com.taskflow_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long projectId,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        List<CommentResponse> comments =
                commentService.getProjectComments(projectId, user);

        return ResponseEntity.ok(
                ApiResponse.success("Comments fetched successfully", comments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(
            @PathVariable Long projectId,
            @PathVariable Long id,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        CommentResponse comment =
                commentService.getCommentById(projectId, id, user);

        return ResponseEntity.ok(
                ApiResponse.success("Comment fetched successfully", comment));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long projectId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        CommentResponse comment =
                commentService.createComment(projectId, request, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Comment created successfully", comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        CommentResponse comment =
                commentService.updateComment(projectId, id, request, user);

        return ResponseEntity.ok(
                ApiResponse.success("Comment updated successfully", comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long projectId,
            @PathVariable Long id,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        commentService.deleteComment(projectId, id, user);

        return ResponseEntity.ok(
                ApiResponse.success("Comment deleted successfully", null));
    }
}
