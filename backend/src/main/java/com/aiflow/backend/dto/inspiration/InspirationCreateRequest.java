package com.aiflow.backend.dto.inspiration;

public class InspirationCreateRequest {
    private String title;
    private String sourcePlatform;
    private String sourceType;
    private String category;
    private String tags;
    private String summary;
    private String hotValue;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSourcePlatform() { return sourcePlatform; }
    public void setSourcePlatform(String sourcePlatform) { this.sourcePlatform = sourcePlatform; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getHotValue() { return hotValue; }
    public void setHotValue(String hotValue) { this.hotValue = hotValue; }
}
