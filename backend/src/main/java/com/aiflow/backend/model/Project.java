package com.aiflow.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Project {
    private Long id;
    private String name;
    private String sourceType;
    private String sourceTitle;
    private String summary;
    private String genreTags;
    private String status;
    private Date createTime;
    private Date updateTime;

}
