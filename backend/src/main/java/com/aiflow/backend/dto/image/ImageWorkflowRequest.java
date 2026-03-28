package com.aiflow.backend.dto.image;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getGenreTags() {
        return genreTags;
    }

    public void setGenreTags(String genreTags) {
        this.genreTags = genreTags;
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
