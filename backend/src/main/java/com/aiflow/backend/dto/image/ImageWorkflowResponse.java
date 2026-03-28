package com.aiflow.backend.dto.image;

import com.aiflow.backend.model.Project;

import java.util.List;

public class ImageWorkflowResponse {
    private Project project;
    private List<ImageWorkflowItem> items;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ImageWorkflowItem> getItems() {
        return items;
    }

    public void setItems(List<ImageWorkflowItem> items) {
        this.items = items;
    }
}
