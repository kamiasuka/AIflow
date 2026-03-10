package com.aiflow.backend.model;

import java.util.Date;

/**
 * 视频模型类
 * 用于存储生成的视频信息
 */
public class Video {
    /**
     * 视频ID
     */
    private Long id;
    
    /**
     * 关联的剧本ID
     */
    private Long scriptId;
    
    /**
     * 视频URL
     */
    private String videoUrl;
    
    /**
     * 视频长度（秒）
     */
    private Integer length;
    
    /**
     * 视频状态（success/failed/pending）
     */
    private String status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}