package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.dto.script.ScriptGenerateRequest;
import com.aiflow.backend.dto.script.ScriptGenerateResponse;
import com.aiflow.backend.model.ModelConfig;
import com.aiflow.backend.service.ModelConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScriptServiceImplTest {

    @Mock
    private ModelConfigService modelConfigService;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private ScriptServiceImpl scriptService;

    @Test
    void generateScriptRequiresEnabledModelConfig() {
        ModelConfig config = new ModelConfig();
        config.setId(1L);
        config.setIsEnabled(0);
        when(modelConfigService.getConfigById(1L)).thenReturn(config);

        ScriptGenerateRequest request = new ScriptGenerateRequest();
        request.setStoryInfo("story");
        request.setPremise("premise");
        request.setModelConfigId(1L);

        assertThrows(ServiceException.class, () -> scriptService.generateScript(request));
    }

    @Test
    void generateScriptUsesResolvedModelConfig() {
        ModelConfig config = new ModelConfig();
        config.setId(2L);
        config.setIsEnabled(1);
        when(modelConfigService.getConfigById(2L)).thenReturn(config);

        ScriptGenerateResponse response = new ScriptGenerateResponse();
        response.setStoryScript("ok");
        when(commonService.callAIAPI("story", "premise", config)).thenReturn(response);

        ScriptGenerateRequest request = new ScriptGenerateRequest();
        request.setStoryInfo("story");
        request.setPremise("premise");
        request.setModelConfigId(2L);

        ScriptGenerateResponse result = scriptService.generateScript(request);

        assertEquals("ok", result.getStoryScript());
        verify(commonService).callAIAPI("story", "premise", config);
    }
}