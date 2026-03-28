package com.aiflow.backend.service;

import com.aiflow.backend.model.GenerationTask;

import java.util.List;

public interface GenerationTaskService {
    GenerationTask createTask(GenerationTask task);

    GenerationTask getTaskById(Long id);

    List<GenerationTask> getTasksByProjectId(Long projectId);
}
