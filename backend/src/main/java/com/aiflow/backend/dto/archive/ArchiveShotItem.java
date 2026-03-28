package com.aiflow.backend.dto.archive;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArchiveShotItem {
    private Long shotId;
    private Integer shotNo;
    private String sceneTitle;
    private String actionDesc;
    private String promptText;
    private String imageStatus;
    private String videoStatus;
    private String imageUrl;
    private String videoUrl;

}
