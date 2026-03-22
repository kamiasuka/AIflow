package com.aiflow.backend.controller;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.model.ModelConfig;
import com.aiflow.backend.service.ModelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 大模型配置控制器
 * 处理大模型配置相关的HTTP请求
 */
@RestController
@RequestMapping("/api/model-config")
public class ModelConfigController {

    @Autowired
    private ModelConfigService modelConfigService;

    /**
     * 获取所有配置
     */
    @GetMapping("/list")
    public JsonResult getAllConfigs() {
        List<ModelConfig> configs = modelConfigService.getAllConfigs();
        return JsonResult.ok(configs);
    }

    /**
     * 根据ID获取配置
     */
    @GetMapping("/{id}")
    public JsonResult getConfigById(@PathVariable Long id) {
        ModelConfig config = modelConfigService.getConfigById(id);
        return JsonResult.ok(config);
    }

    /**
     * 根据模型名称获取配置
     */
    @GetMapping("/name/{modelName}")
    public JsonResult getConfigsByName(@PathVariable String modelName) {
        List<ModelConfig> configs = modelConfigService.getConfigsByName(modelName);
        return JsonResult.ok(configs);
    }

    /**
     * 获取启用的配置
     */
    @GetMapping("/enabled")
    public JsonResult getEnabledConfigs() {
        List<ModelConfig> configs = modelConfigService.getEnabledConfigs();
        return JsonResult.ok(configs);
    }

    /**
     * 获取默认配置
     */
    @GetMapping("/default")
    public JsonResult getDefaultConfig() {
        ModelConfig config = modelConfigService.getDefaultConfig();
        return JsonResult.ok(config);
    }

    /**
     * 添加配置
     */
    @PostMapping
    public JsonResult addConfig(@RequestBody ModelConfig modelConfig) {
        ModelConfig savedConfig = modelConfigService.addConfig(modelConfig);
        return JsonResult.ok(savedConfig);
    }

    /**
     * 更新配置
     */
    @PutMapping("/{id}")
    public JsonResult updateConfig(@PathVariable Long id, @RequestBody ModelConfig modelConfig) {
        modelConfig.setId(id);
        ModelConfig updatedConfig = modelConfigService.updateConfig(modelConfig);
        return JsonResult.ok(updatedConfig);
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{id}")
    public JsonResult deleteConfig(@PathVariable Long id) {
        modelConfigService.deleteConfig(id);
        return JsonResult.ok("删除成功");
    }

    /**
     * 设置默认配置
     */
    @PutMapping("/{id}/default")
    public JsonResult setDefaultConfig(@PathVariable Long id) {
        modelConfigService.setDefaultConfig(id);
        return JsonResult.ok("设置成功");
    }
}
