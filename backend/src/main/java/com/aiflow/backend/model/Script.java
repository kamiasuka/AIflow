package com.aiflow.backend.model;

import java.util.Date;

/**
 * 剧本模型类
 * 用于存储剧本相关信息
 */
public class Script {
    /**
     * 剧本ID
     */
    private Long id;
    
    /**
     * 故事信息
     */
    private String storyInfo;
    
    /**
     * 前提条件
     */
    private String premise;
    
    /**
     * API类型（doubao或deepseek）
     */
    private String apiType;
    
    /**
     * 故事剧本
     */
    private String storyScript;
    
    /**
     * 人物设计
     */
    private String characterDesign;
    
    /**
     * 分镜脚本
     */
    private String shotScript;
    
    /**
     * Prompt提示词
     */
    private String prompt;
    
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

    public String getStoryInfo() {
        return storyInfo;
    }

    public void setStoryInfo(String storyInfo) {
        this.storyInfo = storyInfo;
    }

    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getStoryScript() {
        return storyScript;
    }

    public void setStoryScript(String storyScript) {
        this.storyScript = storyScript;
    }

    public String getCharacterDesign() {
        return characterDesign;
    }

    public void setCharacterDesign(String characterDesign) {
        this.characterDesign = characterDesign;
    }

    public String getShotScript() {
        return shotScript;
    }

    public void setShotScript(String shotScript) {
        this.shotScript = shotScript;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
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