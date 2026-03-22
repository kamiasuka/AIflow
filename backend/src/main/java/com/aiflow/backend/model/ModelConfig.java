package com.aiflow.backend.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 大模型配置实体类
 */
@Data
public class ModelConfig {
    private Long id;
    private String modelName;
    private String apiUrl;
    private String apiKey;
    private String modelVersion;
    private Integer maxTokens;
    private BigDecimal temperature;
    private Integer isEnabled;
    private Integer isDefault;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
