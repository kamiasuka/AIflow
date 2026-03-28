package com.aiflow.backend.dto.project;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectCreateRequest {
    private String name;
    private String sourceType;
    private String sourceTitle;
    private String summary;
    private String genreTags;

}
