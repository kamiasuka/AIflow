package com.aiflow.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Inspiration {
    private Long id;
    private String title;
    private String sourcePlatform;
    private String sourceType;
    private String category;
    private String tags;
    private String summary;
    private String hotValue;
    private String status;
    private Date createTime;
    private Date updateTime;

}
