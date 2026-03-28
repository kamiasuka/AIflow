package com.aiflow.backend.dto.shot;

public class ShotParseRequest {
    private Long projectId;
    private Long scriptId;
    private String storyScript;
    private String shotScript;
    private Integer shotCount;
    private String style;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    public String getStoryScript() {
        return storyScript;
    }

    public void setStoryScript(String storyScript) {
        this.storyScript = storyScript;
    }

    public String getShotScript() {
        return shotScript;
    }

    public void setShotScript(String shotScript) {
        this.shotScript = shotScript;
    }

    public Integer getShotCount() {
        return shotCount;
    }

    public void setShotCount(Integer shotCount) {
        this.shotCount = shotCount;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
