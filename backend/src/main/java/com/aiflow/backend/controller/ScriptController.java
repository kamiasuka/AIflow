package com.aiflow.backend.controller;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dto.script.ScriptGenerateRequest;
import com.aiflow.backend.model.Script;
import com.aiflow.backend.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/script")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    // 生成剧本结果
    @PostMapping("/generate")
    public JsonResult generateScript(@RequestBody ScriptGenerateRequest request) {
        if (request == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "请求体不能为空");
        }
        return JsonResult.ok(scriptService.generateScript(request));
    }

    // 保存剧本记录
    @PostMapping("/save")
    public JsonResult saveScript(@RequestBody Script script) {
        Script savedScript = scriptService.saveScript(script);
        return JsonResult.ok(savedScript);
    }

    // 按 ID 查询剧本
    @GetMapping("/{id}")
    public JsonResult getScript(@PathVariable Long id) {
        Script script = scriptService.getScriptById(id);
        return JsonResult.ok(script);
    }

    @GetMapping("/test")
    public JsonResult test() {
        return JsonResult.ok("服务正常运行");
    }
}