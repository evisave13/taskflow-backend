package com.taskflow_backend.service;

import com.taskflow_backend.dto.CommentRequest;
import com.taskflow_backend.dto.CommentResponse;
import com.taskflow_backend.entity.Comment;
import com.taskflow_backend.entity.Project;
import com.taskflow_backend.entity.User;
import com.taskflow_backend.exception.ResourceNotFoundException;
import com.taskflow_backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public List<CommentResponse> getProjectComments(Long projectId, User user) {
        Project project = projectService.findProjectForUser(projectId, user);
        return commentRepository.findByProject(project).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long projectId,
                                          Long commentId,
                                          User user) {
        Comment comment = findCommentForProject(projectId, commentId, user);
        return toResponse(comment);
    }

    public CommentResponse createComment(Long projectId,
                                         CommentRequest request,
                                         User user) {
        Project project = projectService.findProjectForUser(projectId, user);

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        project.addComment(comment);

        return toResponse(commentRepository.save(comment));
    }

    public CommentResponse updateComment(Long projectId,
                                         Long commentId,
                                         CommentRequest request,
                                         User user) {
        Comment comment = findCommentForProject(projectId, commentId, user);
        comment.setContent(request.getContent());
        return toResponse(commentRepository.save(comment));
    }

    public void deleteComment(Long projectId, Long commentId, User user) {
        Comment comment = findCommentForProject(projectId, commentId, user);
        comment.getProject().removeComment(comment);
        commentRepository.delete(comment);
    }

    private Comment findCommentForProject(Long projectId,
                                          Long commentId,
                                          User user) {
        Project project = projectService.findProjectForUser(projectId, user);
        return commentRepository.findByIdAndProject(commentId, project)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment not found!"));
    }

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .projectId(comment.getProject().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
