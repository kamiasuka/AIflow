package com.aiflow.backend.dto.project;

public class ProjectCreateRequest {
    private String name;
    private String sourceType;
    private String sourceTitle;
    private String summary;
    private String genreTags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGenreTags() {
        return genreTags;
    }

    public void setGenreTags(String genreTags) {
        this.genreTags = genreTags;
    }
}
