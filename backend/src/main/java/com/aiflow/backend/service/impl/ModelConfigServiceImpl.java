package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ModelConfigDao;
import com.aiflow.backend.model.ModelConfig;
import com.aiflow.backend.service.ModelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ModelConfigServiceImpl implements ModelConfigService {

    @Autowired
    private ModelConfigDao modelConfigDao;

    // 查询全部配置
    @Override
    public List<ModelConfig> getAllConfigs() {
        return modelConfigDao.findAll();
    }

    // 按 ID 查询配置
    @Override
    public ModelConfig getConfigById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "配置 ID 不能为空");
        }

        ModelConfig config = modelConfigDao.getById(id);
        if (config == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "配置不存在");
        }
        return config;
    }

    // 按名称查询配置
    @Override
    public List<ModelConfig> getConfigsByName(String modelName) {
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型名称不能为空");
        }
        return modelConfigDao.findByModelName(modelName.trim());
    }

    // 查询启用配置
    @Override
    public List<ModelConfig> getEnabledConfigs() {
        return modelConfigDao.findEnabled();
    }

    // 查询默认配置
    @Override
    public ModelConfig getDefaultConfig() {
        ModelConfig config = modelConfigDao.findDefault();
        if (config != null) {
            return config;
        }

        List<ModelConfig> enabledConfigs = modelConfigDao.findEnabled();
        if (enabledConfigs.isEmpty()) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "没有可用的模型配置");
        }
        return enabledConfigs.get(0);
    }

    // 新增配置
    @Override
    @Transactional
    public ModelConfig addConfig(ModelConfig modelConfig) {
        validateConfig(modelConfig, null);
        normalizeConfig(modelConfig);

        if (isDefault(modelConfig)) {
            modelConfigDao.clearDefault();
        }

        modelConfigDao.insert(modelConfig);
        rebalanceDefaultConfig(null);
        return modelConfigDao.getById(modelConfig.getId());
    }

    // 更新配置
    @Override
    @Transactional
    public ModelConfig updateConfig(ModelConfig modelConfig) {
        if (modelConfig.getId() == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "配置 ID 不能为空");
        }

        ModelConfig existingConfig = getConfigById(modelConfig.getId());
        validateConfig(modelConfig, modelConfig.getId());
        normalizeConfig(modelConfig);

        if (isDefault(modelConfig)) {
            modelConfigDao.clearDefault();
        } else if (existingConfig.getIsDefault() != null && existingConfig.getIsDefault() == 1) {
            modelConfig.setIsDefault(0);
        }

        modelConfigDao.update(modelConfig);
        rebalanceDefaultConfig(modelConfig.getId());
        return modelConfigDao.getById(modelConfig.getId());
    }

    // 删除配置
    @Override
    @Transactional
    public void deleteConfig(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "配置 ID 不能为空");
        }

        getConfigById(id);
        int rows = modelConfigDao.deleteById(id);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "删除配置失败");
        }

        rebalanceDefaultConfig(id);
    }

    // 设置默认配置
    @Override
    @Transactional
    public void setDefaultConfig(Long id) {
        ModelConfig config = getConfigById(id);
        if (config.getIsEnabled() == null || config.getIsEnabled() != 1) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "仅启用状态的模型可以设为默认");
        }

        modelConfigDao.clearDefault();
        modelConfigDao.setDefault(id);
    }

    // 校验配置基础字段
    private void validateConfig(ModelConfig modelConfig, Long currentId) {
        if (modelConfig == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型配置不能为空");
        }
        if (modelConfig.getModelName() == null || modelConfig.getModelName().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型名称不能为空");
        }
        if (modelConfig.getApiUrl() == null || modelConfig.getApiUrl().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "API 地址不能为空");
        }
        if (modelConfig.getApiKey() == null || modelConfig.getApiKey().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "API Key 不能为空");
        }

        List<ModelConfig> configs = modelConfigDao.findByModelName(modelConfig.getModelName().trim());
        boolean duplicateExists = configs.stream()
                .anyMatch(config -> currentId == null || !config.getId().equals(currentId));
        if (duplicateExists) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型名称已存在，请使用唯一名称");
        }

        if (isDefault(modelConfig) && (modelConfig.getIsEnabled() == null || modelConfig.getIsEnabled() != 1)) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "默认模型必须处于启用状态");
        }
    }

    // 补齐默认值
    private void normalizeConfig(ModelConfig modelConfig) {
        modelConfig.setModelName(modelConfig.getModelName().trim());
        modelConfig.setApiUrl(modelConfig.getApiUrl().trim());
        modelConfig.setApiKey(modelConfig.getApiKey().trim());

        if (modelConfig.getIsEnabled() == null) {
            modelConfig.setIsEnabled(1);
        }
        if (modelConfig.getIsDefault() == null) {
            modelConfig.setIsDefault(0);
        }
        if (modelConfig.getMaxTokens() == null) {
            modelConfig.setMaxTokens(4096);
        }
        if (modelConfig.getTemperature() == null) {
            modelConfig.setTemperature(new BigDecimal("0.7"));
        }
    }

    // 判断是否默认
    private boolean isDefault(ModelConfig modelConfig) {
        return modelConfig.getIsDefault() != null && modelConfig.getIsDefault() == 1;
    }

    // 回填默认配置
    private void rebalanceDefaultConfig(Long preferredId) {
        ModelConfig defaultConfig = modelConfigDao.findDefault();
        if (defaultConfig != null) {
            return;
        }

        List<ModelConfig> enabledConfigs = modelConfigDao.findEnabled();
        if (enabledConfigs.isEmpty()) {
            return;
        }

        Long candidateId = enabledConfigs.get(0).getId();
        if (preferredId != null) {
            for (ModelConfig config : enabledConfigs) {
                if (!config.getId().equals(preferredId)) {
                    candidateId = config.getId();
                    break;
                }
            }
        }

        modelConfigDao.clearDefault();
        modelConfigDao.setDefault(candidateId);
    }
}
