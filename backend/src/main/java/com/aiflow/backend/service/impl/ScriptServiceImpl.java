package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ScriptDao;
import com.aiflow.backend.model.ModelConfig;
import com.aiflow.backend.model.Script;
import com.aiflow.backend.service.ModelConfigService;
import com.aiflow.backend.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private ScriptDao scriptDao;
    @Autowired
    private ModelConfigService modelConfigService;
    @Autowired
    private CommonService commonService;

    @Override
    public Map<String, Object> generateScript(String storyInfo, String premise, String apiType) {
        if (storyInfo == null || storyInfo.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "故事信息不能为空");
        }
        if (premise == null || premise.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "前提条件不能为空");
        }
        if (apiType == null || apiType.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "API类型不能为空");
        }

        List<ModelConfig> configs = modelConfigService.getConfigsByName(apiType.trim());
        if (configs == null || configs.isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "未找到[" + apiType + "]模型的配置信息，请先在设置中添加");
        }

        ModelConfig config = configs.get(0);
        if (config.getIsEnabled() != 1) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "[" + apiType + "]模型已禁用，请启用后重试");
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
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "剧本ID不能为空");
        }
        Script script = scriptDao.getById(id);
        if (script == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "剧本不存在");
        }
        return script;
    }
}
