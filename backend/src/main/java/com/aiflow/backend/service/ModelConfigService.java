package com.aiflow.backend.service;

import com.aiflow.backend.model.ModelConfig;
import java.util.List;

/**
 * 大模型配置服务接口
 */
public interface ModelConfigService {
    /**
     * 查询所有配置
     */
    List<ModelConfig> getAllConfigs();

    /**
     * 根据ID查询配置
     */
    ModelConfig getConfigById(Long id);

    /**
     * 根据模型名称查询配置
     */
    List<ModelConfig> getConfigsByName(String modelName);

    /**
     * 查询启用的配置
     */
    List<ModelConfig> getEnabledConfigs();

    /**
     * 查询默认配置
     */
    ModelConfig getDefaultConfig();

    /**
     * 添加配置
     */
    ModelConfig addConfig(ModelConfig modelConfig);

    /**
     * 更新配置
     */
    ModelConfig updateConfig(ModelConfig modelConfig);

    /**
     * 删除配置
     */
    void deleteConfig(Long id);

    /**
     * 设置默认配置
     */
    void setDefaultConfig(Long id);
}
