package com.taskflow_backend.controller;

import com.taskflow_backend.dto.ApiResponse;
import com.taskflow_backend.dto.ProjectRequest;
import com.taskflow_backend.dto.ProjectResponse;
import com.taskflow_backend.entity.User;
import com.taskflow_backend.service.ProjectService;
import com.taskflow_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjects(
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        List<ProjectResponse> projects = projectService.getUserProjects(user);

        return ResponseEntity.ok(
                ApiResponse.success("Projects fetched successfully", projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(
            @PathVariable Long id,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        ProjectResponse project = projectService.getProjectById(id, user);

        return ResponseEntity.ok(
                ApiResponse.success("Project fetched successfully", project));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        ProjectResponse project = projectService.createProject(request, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Project created successfully", project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        ProjectResponse project = projectService.updateProject(id, request, user);

        return ResponseEntity.ok(
                ApiResponse.success("Project updated successfully", project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        projectService.deleteProject(id, user);

        return ResponseEntity.ok(
                ApiResponse.success("Project deleted successfully", null));
    }
}
