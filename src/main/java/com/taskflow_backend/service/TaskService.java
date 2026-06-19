package com.taskflow_backend.service;

import com.taskflow_backend.entity.Task;
import com.taskflow_backend.entity.User;
import com.taskflow_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // Naya task banao
    public Task createTask(String title,
                          String description,
                          User user) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus("TODO");
        task.setUser(user);
        return taskRepository.save(task);
    }

    // User ki saari tasks fetch karo
    public List<Task> getUserTasks(User user) {
        return taskRepository.findByUser(user);
    }

    // Task update karo
    public Task updateTask(Long taskId,
                          String title,
                          String description,
                          String status) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> 
                new RuntimeException("Task not found!"));

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    // Task delete karo
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    // File URL update karo (S3 upload ke baad)
    public Task updateFileUrl(Long taskId, 
                             String fileUrl) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> 
                new RuntimeException("Task not found!"));
        task.setFileUrl(fileUrl);
        return taskRepository.save(task);
    }
}