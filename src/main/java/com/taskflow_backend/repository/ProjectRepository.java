package com.taskflow_backend.repository;

import com.taskflow_backend.entity.Project;
import com.taskflow_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByUser(User user);

    Optional<Project> findByIdAndUser(Long id, User user);

    boolean existsByNameAndUser(String name, User user);
}
