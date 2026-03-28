package com.aiflow.backend.dto.archive;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
