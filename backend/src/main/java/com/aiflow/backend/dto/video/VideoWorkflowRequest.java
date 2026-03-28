package com.aiflow.backend.dto.video;

public class VideoWorkflowRequest {
    private Long projectId;
    private Long shotId;
    private Long scriptId;
    private String script;
    private String imageUrl;
    private String motionPrompt;
    private String cameraMotion;
    private Integer videoLength;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getShotId() {
        return shotId;
    }

    public void setShotId(Long shotId) {
        this.shotId = shotId;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMotionPrompt() {
        return motionPrompt;
    }

    public void setMotionPrompt(String motionPrompt) {
        this.motionPrompt = motionPrompt;
    }

    public String getCameraMotion() {
        return cameraMotion;
    }

    public void setCameraMotion(String cameraMotion) {
        this.cameraMotion = cameraMotion;
    }

    public Integer getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(Integer videoLength) {
        this.videoLength = videoLength;
    }
}
