package com.aiflow.backend.controller;

import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.model.ModelConfig;
import com.aiflow.backend.service.ModelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/model-config")
public class ModelConfigController {

    @Autowired
    private ModelConfigService modelConfigService;

    // 查询全部模型配置
    @GetMapping("/list")
    public JsonResult getAllConfigs() {
        List<ModelConfig> configs = modelConfigService.getAllConfigs();
        return JsonResult.ok(configs);
    }

    // 按 ID 查询配置
    @GetMapping("/{id}")
    public JsonResult getConfigById(@PathVariable Long id) {
        ModelConfig config = modelConfigService.getConfigById(id);
        return JsonResult.ok(config);
    }

    // 按名称查询配置
    @GetMapping("/name/{modelName}")
    public JsonResult getConfigsByName(@PathVariable String modelName) {
        List<ModelConfig> configs = modelConfigService.getConfigsByName(modelName);
        return JsonResult.ok(configs);
    }

    // 查询启用配置
    @GetMapping("/enabled")
    public JsonResult getEnabledConfigs() {
        List<ModelConfig> configs = modelConfigService.getEnabledConfigs();
        return JsonResult.ok(configs);
    }

    // 查询默认配置
    @GetMapping("/default")
    public JsonResult getDefaultConfig() {
        ModelConfig config = modelConfigService.getDefaultConfig();
        return JsonResult.ok(config);
    }

    // 新增模型配置
    @PostMapping
    public JsonResult addConfig(@RequestBody ModelConfig modelConfig) {
        ModelConfig savedConfig = modelConfigService.addConfig(modelConfig);
        return JsonResult.ok(savedConfig);
    }

    // 更新模型配置
    @PutMapping("/{id}")
    public JsonResult updateConfig(@PathVariable Long id, @RequestBody ModelConfig modelConfig) {
        modelConfig.setId(id);
        ModelConfig updatedConfig = modelConfigService.updateConfig(modelConfig);
        return JsonResult.ok(updatedConfig);
    }

    // 删除模型配置
    @DeleteMapping("/{id}")
    public JsonResult deleteConfig(@PathVariable Long id) {
        modelConfigService.deleteConfig(id);
        return JsonResult.ok("删除成功");
    }

    // 设置默认模型配置
    @PutMapping("/{id}/default")
    public JsonResult setDefaultConfig(@PathVariable Long id) {
        modelConfigService.setDefaultConfig(id);
        return JsonResult.ok("设置成功");
    }
}