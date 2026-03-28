package com.aiflow.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Shot {
    private Long id;
    private Long projectId;
    private Long scriptId;
    private Integer shotNo;
    private String sceneTitle;
    private String subject;
    private String actionDesc;
    private String envDesc;
    private String cameraDesc;
    private String styleDesc;
    private Integer durationSec;
    private String promptText;
    private String negativePrompt;
    private String imageStatus;
    private String videoStatus;
    private Date createTime;
    private Date updateTime;

}
