package com.taskflow_backend.repository;

import com.taskflow_backend.entity.Task;
import com.taskflow_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository 
    extends JpaRepository<Task, Long> {

    // Specific user ki saari tasks
    List<Task> findByUser(User user);

    // Status se filter karo
    List<Task> findByUserAndStatus(
        User user, String status);
}