package com.aiflow.backend.model;

import java.util.Date;

/**
 * 图片模型类
 * 用于存储生成的分镜图片信息
 */
public class Image {
    /**
     * 图片ID
     */
    private Long id;
    
    /**
     * 关联的剧本ID
     */
    private Long scriptId;
    
    /**
     * 图片URL
     */
    private String imageUrl;
    
    /**
     * 分镜索引
     */
    private Integer shotIndex;
    
    /**
     * 图片风格
     */
    private String style;
    
    /**
     * 创建时间
     */
    private Date createTime;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getShotIndex() {
        return shotIndex;
    }

    public void setShotIndex(Integer shotIndex) {
        this.shotIndex = shotIndex;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}