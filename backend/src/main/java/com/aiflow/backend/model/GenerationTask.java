package com.aiflow.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class GenerationTask {
    private Long id;
    private Long projectId;
    private Long shotId;
    private String taskType;
    private String provider;
    private String modelName;
    private String promptText;
    private String status;
    private String resultUrl;
    private String errorMessage;
    private Date createTime;
    private Date updateTime;

}
