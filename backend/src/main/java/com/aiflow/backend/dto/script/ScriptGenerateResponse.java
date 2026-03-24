package com.aiflow.backend.dto.script;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScriptGenerateResponse {
    private String storyScript;
    private String characterDesign;
    private String shotScript;
    private String prompt;
    private String rawContent;
}
