package com.aiflow.backend.service;

import com.aiflow.backend.model.Script;

import java.util.Map;

public interface ScriptService {
    Map<String, Object> generateScript(String storyInfo, String premise, String apiType);
    Script saveScript(Script script);
    Script getScriptById(Long id);
}