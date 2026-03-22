package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ModelConfigDao;
import com.aiflow.backend.model.ModelConfig;
import com.aiflow.backend.service.ModelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 大模型配置服务实现类
 */
@Service
public class ModelConfigServiceImpl implements ModelConfigService {

    @Autowired
    private ModelConfigDao modelConfigDao;

    @Override
    public List<ModelConfig> getAllConfigs() {
        return modelConfigDao.findAll();
    }

    @Override
    public ModelConfig getConfigById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "配置ID不能为空");
        }
        ModelConfig config = modelConfigDao.getById(id);
        if (config == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "配置不存在");
        }
        return config;
    }

    @Override
    public List<ModelConfig> getConfigsByName(String modelName) {
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型名称不能为空");
        }
        return modelConfigDao.findByModelName(modelName);
    }

    @Override
    public List<ModelConfig> getEnabledConfigs() {
        return modelConfigDao.findEnabled();
    }

    @Override
    public ModelConfig getDefaultConfig() {
        ModelConfig config = modelConfigDao.findDefault();
        if (config == null) {
            List<ModelConfig> enabledConfigs = modelConfigDao.findEnabled();
            if (!enabledConfigs.isEmpty()) {
                return enabledConfigs.get(0);
            }
            throw new ServiceException(StatusCode.OPERATION_FAILED, "没有可用的模型配置");
        }
        return config;
    }

    @Override
    @Transactional
    public ModelConfig addConfig(ModelConfig modelConfig) {
        validateConfig(modelConfig);

        if (modelConfig.getIsEnabled() == null) {
            modelConfig.setIsEnabled(1);
        }
        if (modelConfig.getMaxTokens() == null) {
            modelConfig.setMaxTokens(4096);
        }
        if (modelConfig.getTemperature() == null) {
            modelConfig.setTemperature(new java.math.BigDecimal("0.7"));
        }

        if (modelConfig.getIsDefault() != null && modelConfig.getIsDefault() == 1) {
            modelConfigDao.clearDefault();
        }

        modelConfigDao.insert(modelConfig);

        if (modelConfig.getIsDefault() != null && modelConfig.getIsDefault() == 1) {
            modelConfigDao.setDefault(modelConfig.getId());
        }

        return modelConfig;
    }

    @Override
    @Transactional
    public ModelConfig updateConfig(ModelConfig modelConfig) {
        if (modelConfig.getId() == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "配置ID不能为空");
        }

        ModelConfig existingConfig = getConfigById(modelConfig.getId());
        validateConfig(modelConfig);

        if (modelConfig.getIsDefault() != null && modelConfig.getIsDefault() == 1) {
            modelConfigDao.clearDefault();
        }

        modelConfigDao.update(modelConfig);

        if (modelConfig.getIsDefault() != null && modelConfig.getIsDefault() == 1) {
            modelConfigDao.setDefault(modelConfig.getId());
        }

        return modelConfig;
    }

    @Override
    @Transactional
    public void deleteConfig(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "配置ID不能为空");
        }

        ModelConfig config = getConfigById(id);
        int rows = modelConfigDao.deleteById(id);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "删除配置失败");
        }
    }

    @Override
    @Transactional
    public void setDefaultConfig(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "配置ID不能为空");
        }

        ModelConfig config = getConfigById(id);
        if (config.getIsEnabled() != 1) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "只能将已启用的配置设为默认");
        }

        modelConfigDao.clearDefault();
        modelConfigDao.setDefault(id);
    }

    private void validateConfig(ModelConfig modelConfig) {
        if (modelConfig.getModelName() == null || modelConfig.getModelName().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型名称不能为空");
        }
        if (modelConfig.getApiUrl() == null || modelConfig.getApiUrl().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "API地址不能为空");
        }
        if (modelConfig.getApiKey() == null || modelConfig.getApiKey().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "API密钥不能为空");
        }
    }
}
