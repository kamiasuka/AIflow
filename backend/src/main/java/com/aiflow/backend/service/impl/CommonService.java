package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.model.ModelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据配置调用AI API生成剧本
     */
    public Map<String, Object> callAIAPI(String storyInfo, String premise, ModelConfig config) {
        if (config == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型配置不能为空");
        }

        String apiUrl = config.getApiUrl();
        String apiKey = config.getApiKey();
        String model = config.getModelVersion();
        BigDecimal temperature = config.getTemperature();
        Integer maxTokens = config.getMaxTokens();
        String modelName = config.getModelName();

        if (temperature == null) {
            temperature = new BigDecimal("0.7");
        }
        if (maxTokens == null) {
            maxTokens = 2000;
        }

        String prompt = buildPrompt(storyInfo, premise, modelName);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", temperature.doubleValue());
        requestBody.put("max_tokens", maxTokens);

        ArrayList<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        Map<String, Object> response;
        try {
            response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            ).getBody();
        } catch (Exception ex) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "调用AI接口失败: " + ex.getMessage());
        }

        return parseAIResponse(response);
    }

    private String buildPrompt(String storyInfo, String premise, String modelName) {
        String prompt;
        if (modelName != null && modelName.toLowerCase().contains("doubao")) {
            prompt = "请根据以下信息生成一个完整的故事剧本：\n"
                    + "故事信息：" + storyInfo + "\n"
                    + "前提条件：" + premise + "\n"
                    + "要求：\n"
                    + "1. 生成完整的故事剧本\n"
                    + "2. 包含详细的人物设计\n"
                    + "3. 提供分镜脚本\n"
                    + "4. 为每个分镜生成适合的Prompt提示词\n";
        } else {
            prompt = "Generate a complete story script based on the following information:\n"
                    + "Story Info: " + storyInfo + "\n"
                    + "Premise: " + premise + "\n"
                    + "Requirements:\n"
                    + "1. Generate a complete story script\n"
                    + "2. Include detailed character designs\n"
                    + "3. Provide shot scripts\n"
                    + "4. Generate suitable prompt words for each shot\n";
        }
        return prompt;
    }

    private Map<String, Object> parseAIResponse(Map<String, Object> response) {
        Map<String, Object> result = new HashMap<>();

        if (response == null || !response.containsKey("choices")) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "AI接口响应格式错误");
        }

        ArrayList<Map<String, Object>> choices = (ArrayList<Map<String, Object>>) response.get("choices");
        if (choices.isEmpty()) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "AI接口响应中没有choices数据");
        }

        Map<String, Object> choice = choices.get(0);
        Map<String, Object> messageObj = (Map<String, Object>) choice.get("message");
        if (messageObj == null || !messageObj.containsKey("content")) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "AI接口响应中没有content数据");
        }

        String content = (String) messageObj.get("content");

        String[] parts = content.split("\\n\\n");
        for (String part : parts) {
            if (part.contains("故事剧本") || part.contains("story script")) {
                result.put("storyScript", part);
            } else if (part.contains("人物设计") || part.contains("character design")) {
                result.put("characterDesign", part);
            } else if (part.contains("分镜脚本") || part.contains("shot script")) {
                result.put("shotScript", part);
            } else if (part.contains("Prompt") || part.contains("prompt")) {
                result.put("prompt", part);
            }
        }

        if (!result.containsKey("storyScript")) {
            result.put("storyScript", content);
        }
        if (!result.containsKey("characterDesign")) {
            result.put("characterDesign", "");
        }
        if (!result.containsKey("shotScript")) {
            result.put("shotScript", "");
        }
        if (!result.containsKey("prompt")) {
            result.put("prompt", "");
        }

        return result;
    }
}
