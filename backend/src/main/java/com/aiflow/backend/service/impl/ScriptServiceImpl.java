package com.aiflow.backend.service.impl;

import com.aiflow.backend.dao.ScriptDao;
import com.aiflow.backend.model.Script;
import com.aiflow.backend.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private ScriptDao scriptDao;

    @Override
    public Map<String, Object> generateScript(String storyInfo, String premise, String apiType) {
        // 模拟API调用，实际项目中需要调用真实的AI API
        Map<String, Object> result = new HashMap<>();
        result.put("storyScript", "这是生成的故事剧本...");
        result.put("characterDesign", "这是人物设计...");
        result.put("shotScript", "这是分镜脚本...");
        result.put("prompt", "这是Prompt提示词...");
        return result;
    }

    @Override
    public Script saveScript(Script script) {
        // 使用DAO保存剧本到数据库
        scriptDao.save(script);
        return script;
    }

    @Override
    public Script getScriptById(Long id) {
        // 使用DAO从数据库查询剧本
        return scriptDao.getById(id);
    }
}