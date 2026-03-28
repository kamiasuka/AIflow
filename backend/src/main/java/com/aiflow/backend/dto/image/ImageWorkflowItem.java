package com.aiflow.backend.dto.image;

import com.aiflow.backend.model.Shot;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageWorkflowItem {
    private Shot shot;
    private Long taskId;
    private String taskStatus;
    private String imageUrl;

}
