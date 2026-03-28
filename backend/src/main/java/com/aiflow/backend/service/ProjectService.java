package com.aiflow.backend.service;

import com.aiflow.backend.model.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project);

    Project getProjectById(Long id);

    List<Project> getAllProjects();
}
