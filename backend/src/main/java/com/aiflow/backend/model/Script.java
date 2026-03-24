package com.aiflow.backend.model;

import java.util.Date;

public class Script {
    private Long id;
    private String storyInfo;
    private String premise;
    private Long modelConfigId;
    private String storyScript;
    private String characterDesign;
    private String shotScript;
    private String prompt;
    private Date createTime;
    private Date updateTime;

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

    public Long getModelConfigId() {
        return modelConfigId;
    }

    public void setModelConfigId(Long modelConfigId) {
        this.modelConfigId = modelConfigId;
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
