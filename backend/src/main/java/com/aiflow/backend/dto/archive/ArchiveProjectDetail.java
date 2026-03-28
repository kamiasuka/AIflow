package com.aiflow.backend.dto.archive;

import com.aiflow.backend.model.GenerationTask;
import com.aiflow.backend.model.Project;

import java.util.List;

public class ArchiveProjectDetail {
    private Project project;
    private List<ArchiveShotItem> shotList;
    private List<GenerationTask> taskList;

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public List<ArchiveShotItem> getShotList() { return shotList; }
    public void setShotList(List<ArchiveShotItem> shotList) { this.shotList = shotList; }
    public List<GenerationTask> getTaskList() { return taskList; }
    public void setTaskList(List<GenerationTask> taskList) { this.taskList = taskList; }
}
