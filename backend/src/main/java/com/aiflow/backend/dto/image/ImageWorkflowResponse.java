package com.aiflow.backend.dto.image;

import com.aiflow.backend.model.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ImageWorkflowResponse {
    private Project project;
    private List<ImageWorkflowItem> items;

}
