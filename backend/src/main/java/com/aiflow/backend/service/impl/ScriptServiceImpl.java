package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ScriptDao;
import com.aiflow.backend.dto.script.ScriptGenerateRequest;
import com.aiflow.backend.dto.script.ScriptGenerateResponse;
import com.aiflow.backend.model.ModelConfig;
import com.aiflow.backend.model.Script;
import com.aiflow.backend.service.ModelConfigService;
import com.aiflow.backend.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private ScriptDao scriptDao;

    @Autowired
    private ModelConfigService modelConfigService;

    @Autowired
    private CommonService commonService;

    @Override
    public ScriptGenerateResponse generateScript(ScriptGenerateRequest request) {
        if (request == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "请求体不能为空");
        }

        String storyInfo = normalizeText(request.getStoryInfo(), "故事信息");
        String premise = normalizeText(request.getPremise(), "前提条件");
        Long modelConfigId = request.getModelConfigId();
        if (modelConfigId == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型配置不能为空");
        }

        ModelConfig config = modelConfigService.getConfigById(modelConfigId);
        if (config.getIsEnabled() == null || config.getIsEnabled() != 1) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "当前模型已禁用，请启用后重试");
        }

        return commonService.callAIAPI(storyInfo, premise, config);
    }

    @Override
    public Script saveScript(Script script) {
        if (script == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "剧本对象不能为空");
        }
        int rows = scriptDao.save(script);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "保存剧本失败");
        }
        return script;
    }

    @Override
    public Script getScriptById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "剧本 ID 不能为空");
        }
        Script script = scriptDao.getById(id);
        if (script == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "剧本不存在");
        }
        return script;
    }

    // 统一清理空值和空白字符
    private String normalizeText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, fieldName + "不能为空");
        }
        return value.trim();
    }
}
