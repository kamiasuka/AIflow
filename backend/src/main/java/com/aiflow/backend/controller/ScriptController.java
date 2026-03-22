package com.aiflow.backend.controller;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.common.response.StatusCode;
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
     * @return 统一的JSON返回结果
     */
    @PostMapping("/generate")
    public JsonResult generateScript(@RequestBody Map<String, Object> request) {
        String storyInfo = getRequiredString(request, "storyInfo", "故事信息");
        String premise = getRequiredString(request, "premise", "前提条件");
        String apiType = getRequiredString(request, "apiType", "API类型");

        Map<String, Object> result = scriptService.generateScript(storyInfo, premise, apiType);
        return JsonResult.ok(result);
    }

    /**
     * 保存剧本
     * @param script 剧本对象
     * @return 统一的JSON返回结果
     */
    @PostMapping("/save")
    public JsonResult saveScript(@RequestBody Script script) {
        Script savedScript = scriptService.saveScript(script);
        return JsonResult.ok(savedScript);
    }

    /**
     * 根据ID获取剧本
     * @param id 剧本ID
     * @return 统一的JSON返回结果
     */
    @GetMapping("/{id}")
    public JsonResult getScript(@PathVariable Long id) {
        Script script = scriptService.getScriptById(id);
        return JsonResult.ok(script);
    }

    /**
     * 测试接口
     * @return 统一的JSON返回结果
     */
    @GetMapping("/test")
    public JsonResult test() {
        return JsonResult.ok("服务正常运行");
    }

    private String getRequiredString(Map<String, Object> request, String key, String fieldName) {
        Object value = request.get(key);
        if (!(value instanceof String strValue) || strValue.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, fieldName + "不能为空");
        }
        return strValue.trim();
    }
}
