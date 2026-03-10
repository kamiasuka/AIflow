package com.aiflow.backend.controller;

import com.aiflow.backend.model.Script;
import com.aiflow.backend.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 剧本控制器
 * 处理剧本相关的HTTP请求
 */
@RestController
@RequestMapping("/api/script")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    /**
     * 生成剧本
     * @param request 请求参数，包含故事信息、前提条件和API类型
     * @return 生成的剧本信息，包含故事剧本、人物设计、分镜脚本和Prompt提示词
     */
    @PostMapping("/generate")
    public Map<String, Object> generateScript(@RequestBody Map<String, Object> request) {
        String storyInfo = (String) request.get("storyInfo");
        String premise = (String) request.get("premise");
        String apiType = (String) request.get("apiType");
        return scriptService.generateScript(storyInfo, premise, apiType);
    }

    /**
     * 保存剧本
     * @param script 剧本对象
     * @return 保存后的剧本对象
     */
    @PostMapping("/save")
    public Script saveScript(@RequestBody Script script) {
        return scriptService.saveScript(script);
    }

    /**
     * 根据ID获取剧本
     * @param id 剧本ID
     * @return 剧本对象
     */
    @GetMapping("/{id}")
    public Script getScript(@PathVariable Long id) {
        return scriptService.getScriptById(id);
    }

    @GetMapping("/test")
    public String test()
    {
        return "hello";
    }
}