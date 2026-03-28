package com.aiflow.backend.dto.inspiration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InspirationCreateRequest {
    private String title;
    private String sourcePlatform;
    private String sourceType;
    private String category;
    private String tags;
    private String summary;
    private String hotValue;

}
