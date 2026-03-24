package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.dao.ModelConfigDao;
import com.aiflow.backend.model.ModelConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModelConfigServiceImplTest {

    @Mock
    private ModelConfigDao modelConfigDao;

    @InjectMocks
    private ModelConfigServiceImpl modelConfigService;

    @Test
    void addConfigRejectsDisabledDefaultModel() {
        ModelConfig config = buildConfig();
        config.setIsEnabled(0);
        config.setIsDefault(1);

        assertThrows(ServiceException.class, () -> modelConfigService.addConfig(config));
        verify(modelConfigDao, never()).insert(any());
    }

    @Test
    void addConfigRejectsDuplicateModelName() {
        ModelConfig config = buildConfig();
        when(modelConfigDao.findByModelName("Doubao")).thenReturn(List.of(existingConfig(2L, "Doubao")));

        assertThrows(ServiceException.class, () -> modelConfigService.addConfig(config));
    }

    @Test
    void deleteDefaultConfigFallsBackToFirstEnabledConfig() {
        ModelConfig deletingConfig = existingConfig(1L, "Doubao");
        deletingConfig.setIsDefault(1);

        ModelConfig fallbackConfig = existingConfig(2L, "DeepSeek");
        when(modelConfigDao.getById(1L)).thenReturn(deletingConfig);
        when(modelConfigDao.deleteById(1L)).thenReturn(1);
        when(modelConfigDao.findDefault()).thenReturn(null);
        when(modelConfigDao.findEnabled()).thenReturn(List.of(fallbackConfig));

        modelConfigService.deleteConfig(1L);

        verify(modelConfigDao).setDefault(2L);
    }

    @Test
    void addConfigAppliesDefaultsAndReturnsPersistedRecord() {
        ModelConfig config = buildConfig();
        config.setMaxTokens(null);
        config.setTemperature(null);
        config.setIsEnabled(null);
        config.setIsDefault(null);

        when(modelConfigDao.findByModelName("Doubao")).thenReturn(List.of());
        doAnswer(invocation -> {
            ModelConfig argument = invocation.getArgument(0);
            argument.setId(3L);
            return 1;
        }).when(modelConfigDao).insert(any(ModelConfig.class));
        when(modelConfigDao.findDefault()).thenReturn(null);
        when(modelConfigDao.findEnabled()).thenReturn(List.of(existingConfig(3L, "Doubao")));
        when(modelConfigDao.getById(3L)).thenReturn(existingConfig(3L, "Doubao"));

        ModelConfig saved = modelConfigService.addConfig(config);

        assertEquals(3L, saved.getId());
        assertEquals(4096, config.getMaxTokens());
        assertEquals(new BigDecimal("0.7"), config.getTemperature());
        assertEquals(1, config.getIsEnabled());
        assertEquals(0, config.getIsDefault());
    }

    private ModelConfig buildConfig() {
        ModelConfig config = new ModelConfig();
        config.setModelName("Doubao");
        config.setApiUrl("https://example.com");
        config.setApiKey("secret");
        config.setModelVersion("v1");
        config.setMaxTokens(2048);
        config.setTemperature(new BigDecimal("0.7"));
        config.setIsEnabled(1);
        config.setIsDefault(0);
        return config;
    }

    private ModelConfig existingConfig(Long id, String modelName) {
        ModelConfig config = buildConfig();
        config.setId(id);
        config.setModelName(modelName);
        return config;
    }
}
