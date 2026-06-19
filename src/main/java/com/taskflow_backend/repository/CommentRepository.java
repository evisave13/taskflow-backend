package com.taskflow_backend.repository;

import com.taskflow_backend.entity.Comment;
import com.taskflow_backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByProject(Project project);

    Optional<Comment> findByIdAndProject(Long id, Project project);
}
