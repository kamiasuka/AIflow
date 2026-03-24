package com.aiflow.backend.service;

import com.aiflow.backend.model.ModelConfig;
import java.util.List;

public interface ModelConfigService {
    // 查询全部配置
    List<ModelConfig> getAllConfigs();

    // 按 ID 查询配置
    ModelConfig getConfigById(Long id);

    // 按名称查询配置
    List<ModelConfig> getConfigsByName(String modelName);

    // 查询启用配置
    List<ModelConfig> getEnabledConfigs();

    // 查询默认配置
    ModelConfig getDefaultConfig();

    // 新增配置
    ModelConfig addConfig(ModelConfig modelConfig);

    // 更新配置
    ModelConfig updateConfig(ModelConfig modelConfig);

    // 删除配置
    void deleteConfig(Long id);

    // 设置默认配置
    void setDefaultConfig(Long id);
}