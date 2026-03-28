package com.aiflow.backend.dto.archive;

public class ArchiveShotItem {
    private Long shotId;
    private Integer shotNo;
    private String sceneTitle;
    private String actionDesc;
    private String promptText;
    private String imageStatus;
    private String videoStatus;
    private String imageUrl;
    private String videoUrl;

    public Long getShotId() { return shotId; }
    public void setShotId(Long shotId) { this.shotId = shotId; }
    public Integer getShotNo() { return shotNo; }
    public void setShotNo(Integer shotNo) { this.shotNo = shotNo; }
    public String getSceneTitle() { return sceneTitle; }
    public void setSceneTitle(String sceneTitle) { this.sceneTitle = sceneTitle; }
    public String getActionDesc() { return actionDesc; }
    public void setActionDesc(String actionDesc) { this.actionDesc = actionDesc; }
    public String getPromptText() { return promptText; }
    public void setPromptText(String promptText) { this.promptText = promptText; }
    public String getImageStatus() { return imageStatus; }
    public void setImageStatus(String imageStatus) { this.imageStatus = imageStatus; }
    public String getVideoStatus() { return videoStatus; }
    public void setVideoStatus(String videoStatus) { this.videoStatus = videoStatus; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
