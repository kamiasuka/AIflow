package com.aiflow.backend.dto.video;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoWorkflowResponse {
    private Long taskId;
    private String taskStatus;
    private String videoUrl;

}
