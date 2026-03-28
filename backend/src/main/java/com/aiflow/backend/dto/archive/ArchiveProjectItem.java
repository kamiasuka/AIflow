package com.aiflow.backend.dto.archive;

public class ArchiveProjectItem {
    private Long id;
    private String name;
    private String sourceType;
    private String sourceTitle;
    private String genreTags;
    private String status;
    private Integer shotCount;
    private Integer imageTaskCount;
    private Integer videoTaskCount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getSourceTitle() { return sourceTitle; }
    public void setSourceTitle(String sourceTitle) { this.sourceTitle = sourceTitle; }
    public String getGenreTags() { return genreTags; }
    public void setGenreTags(String genreTags) { this.genreTags = genreTags; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getShotCount() { return shotCount; }
    public void setShotCount(Integer shotCount) { this.shotCount = shotCount; }
    public Integer getImageTaskCount() { return imageTaskCount; }
    public void setImageTaskCount(Integer imageTaskCount) { this.imageTaskCount = imageTaskCount; }
    public Integer getVideoTaskCount() { return videoTaskCount; }
    public void setVideoTaskCount(Integer videoTaskCount) { this.videoTaskCount = videoTaskCount; }
}
