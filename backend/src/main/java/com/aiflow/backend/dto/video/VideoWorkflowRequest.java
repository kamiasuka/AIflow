package com.aiflow.backend.dto.video;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoWorkflowRequest {
    private Long projectId;
    private Long shotId;
    private Long scriptId;
    private String script;
    private String imageUrl;
    private String motionPrompt;
    private String cameraMotion;
    private Integer videoLength;

}
