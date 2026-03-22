package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonService {

    // API配置
    private static final String DOUBAO_API_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
    private static final String DOUBAO_API_KEY = "your_doubao_api_key_here";
    private static final String DOUBAO_MODEL = "ep-20240310155935-fh465";
    
    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String DEEPSEEK_API_KEY = "sk-6143ba08467e4f1e82ecfdf133477e92";
    private static final String DEEPSEEK_MODEL = "deepseek-chat";
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 调用豆包API生成剧本
     * @param storyInfo 故事信息
     * @param premise 前提条件
     * @return 生成的剧本信息
     * @throws Exception 当API调用失败时抛出异常
     */
    public Map<String, Object> callDoubaoAPI(String storyInfo, String premise) {
        return callAIAPI(storyInfo, premise, "doubao");
    }
    
    /**
     * 调用DeepSeek API生成剧本
     * @param storyInfo 故事信息
     * @param premise 前提条件
     * @return 生成的剧本信息
     * @throws Exception 当API调用失败时抛出异常
     */
    public Map<String, Object> callDeepSeekAPI(String storyInfo, String premise) {

        return callAIAPI(storyInfo, premise, "deepseek");
    }
    
    /**
     * 通用AI API调用方法
     * @param storyInfo 故事信息
     * @param premise 前提条件
     * @param apiType API类型
     * @return 生成的剧本信息
     * @throws Exception 当API调用失败时抛出异常
     */
    private Map<String, Object> callAIAPI(String storyInfo, String premise, String apiType) {
        Map<String, Object> result = new HashMap<>();
        
        // 根据API类型获取配置
        String apiUrl, apiKey, model;
        if ("doubao".equals(apiType)) {
            apiUrl = DOUBAO_API_URL;
            apiKey = DOUBAO_API_KEY;
            model = DOUBAO_MODEL;
        } else if ("deepseek".equals(apiType)) {
            apiUrl = DEEPSEEK_API_URL;
            apiKey = DEEPSEEK_API_KEY;
            model = DEEPSEEK_MODEL;
        } else {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "不支持的API类型: " + apiType);
        }
        
        // 构建提示词
        String prompt;
        if ("doubao".equals(apiType)) {
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
        
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        
        // 构建messages数组
        ArrayList<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);
        
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        
        // 创建请求实体
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        // 发送请求
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


        // 解析响应
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
        
        // 解析生成的内容，提取各个部分
        String[] parts = content.split("\\n\\n");
        for (String part : parts) {
            if (part.contains("故事剧本") || part.contains("story script")) {
                result.put("storyScript", part);
            } else if (part.contains("人物设计") || part.contains("character design")) {
                result.put("characterDesign", part);
            } else if (part.contains("分镜脚本") || part.contains("shot script")) {
                result.put("shotScript", part);
            } else if (part.contains("Prompt提示词") || part.contains("prompt")) {
                result.put("prompt", part);
            }
        }
        
        // 确保所有字段都有值
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
