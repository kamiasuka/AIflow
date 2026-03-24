package com.aiflow.backend.service;

import com.aiflow.backend.dto.script.ScriptGenerateRequest;
import com.aiflow.backend.dto.script.ScriptGenerateResponse;
import com.aiflow.backend.model.Script;

public interface ScriptService {
    // 调用模型生成剧本
    ScriptGenerateResponse generateScript(ScriptGenerateRequest request);

    // 持久化剧本
    Script saveScript(Script script);

    // 查询剧本详情
    Script getScriptById(Long id);
}