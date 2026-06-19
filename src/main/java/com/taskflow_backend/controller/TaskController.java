package com.taskflow_backend.controller;

import com.taskflow_backend.entity.Task;
import com.taskflow_backend.entity.User;
import com.taskflow_backend.service.TaskService;
import com.taskflow_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    // Saari tasks fetch karo
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            Authentication authentication) {

        User user = userService.findByEmail(
            authentication.getName());

        List<Task> tasks =
            taskService.getUserTasks(user);

        return ResponseEntity.ok(tasks);
    }

    // Naya task banao
    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        User user = userService.findByEmail(
            authentication.getName());

        Task task = taskService.createTask(
            request.get("title"),
            request.get("description"),
            user
        );

        return ResponseEntity.ok(task);
    }

    // Task update karo
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Task task = taskService.updateTask(
            id,
            request.get("title"),
            request.get("description"),
            request.get("status")
        );

        return ResponseEntity.ok(task);
    }

    // Task delete karo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);
        return ResponseEntity.ok(
            Map.of("message", "Task deleted!"));
    }
}