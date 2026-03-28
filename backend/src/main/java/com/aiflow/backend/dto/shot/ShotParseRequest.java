package com.aiflow.backend.dto.shot;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShotParseRequest {
    private Long projectId;
    private Long scriptId;
    private String storyScript;
    private String shotScript;
    private Integer shotCount;
    private String style;

}
