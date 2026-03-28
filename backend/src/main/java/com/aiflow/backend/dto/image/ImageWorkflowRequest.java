package com.aiflow.backend.dto.image;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageWorkflowRequest {
    private Long projectId;
    private Long scriptId;
    private String projectName;
    private String sourceTitle;
    private String genreTags;
    private String storyScript;
    private String shotScript;
    private Integer shotCount;
    private String style;

}
