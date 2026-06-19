package com.taskflow_backend.service;

import com.taskflow_backend.dto.CommentResponse;
import com.taskflow_backend.dto.ProjectRequest;
import com.taskflow_backend.dto.ProjectResponse;
import com.taskflow_backend.entity.Comment;
import com.taskflow_backend.entity.Project;
import com.taskflow_backend.entity.User;
import com.taskflow_backend.exception.ConflictException;
import com.taskflow_backend.exception.ResourceNotFoundException;
import com.taskflow_backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getUserProjects(User user) {
        return projectRepository.findByUser(user).stream()
                .map(this::toResponseWithoutComments)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId, User user) {
        Project project = findProjectForUser(projectId, user);
        return toResponseWithComments(project);
    }

    public ProjectResponse createProject(ProjectRequest request, User user) {
        if (projectRepository.existsByNameAndUser(request.getName(), user)) {
            throw new ConflictException("Project with this name already exists!");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setUser(user);

        return toResponseWithoutComments(projectRepository.save(project));
    }

    public ProjectResponse updateProject(Long projectId,
                                         ProjectRequest request,
                                         User user) {
        Project project = findProjectForUser(projectId, user);

        if (!project.getName().equals(request.getName())
                && projectRepository.existsByNameAndUser(request.getName(), user)) {
            throw new ConflictException("Project with this name already exists!");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return toResponseWithoutComments(projectRepository.save(project));
    }

    public void deleteProject(Long projectId, User user) {
        Project project = findProjectForUser(projectId, user);
        projectRepository.delete(project);
    }

    public Project findProjectForUser(Long projectId, User user) {
        return projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found!"));
    }

    private ProjectResponse toResponseWithoutComments(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .build();
    }

    private ProjectResponse toResponseWithComments(Project project) {
        List<CommentResponse> comments = project.getComments().stream()
                .map(this::toCommentResponse)
                .toList();

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .comments(comments)
                .build();
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .projectId(comment.getProject().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
