package com.aiflow.backend.dto.script;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptGenerateRequest {
    private String storyInfo;
    private String premise;
    private Long modelConfigId;
}
