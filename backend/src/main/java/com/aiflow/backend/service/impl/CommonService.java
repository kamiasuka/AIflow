package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dto.script.ScriptGenerateResponse;
import com.aiflow.backend.model.ModelConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 组装请求并解析模型返回
    // 组装请求并解析模型返回
    public ScriptGenerateResponse callAIAPI(String storyInfo, String premise, ModelConfig config) {
        if (config == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "模型配置不能为空");
        }

        Map<String, Object> requestBody = buildChatRequest(storyInfo, premise, config);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, buildHeaders(config));

        Map<String, Object> response;
        try {
            response = restTemplate.exchange(
                    config.getApiUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            ).getBody();
        } catch (Exception ex) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "调用 AI 接口失败: " + ex.getMessage());
        }

        String content = extractContent(response);
        return parseAIResponse(content);
    }

    // 构建请求头
    // 构建请求头
    private HttpHeaders buildHeaders(ModelConfig config) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getApiKey());
        return headers;
    }

    // 构建模型请求体
    // 构建模型请求体
    private Map<String, Object> buildChatRequest(String storyInfo, String premise, ModelConfig config) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", resolveModelName(config));
        requestBody.put("temperature", resolveTemperature(config.getTemperature()));
        requestBody.put("max_tokens", config.getMaxTokens() == null ? 2000 : config.getMaxTokens());
        requestBody.put("messages", buildMessages(storyInfo, premise, config));
        return requestBody;
    }

    // 优先使用模型版本号
    // 优先使用模型版本
    private String resolveModelName(ModelConfig config) {
        if (config.getModelVersion() != null && !config.getModelVersion().trim().isEmpty()) {
            return config.getModelVersion().trim();
        }
        return config.getModelName();
    }

    // 拼接 system 和 user 消息
    // 拼接 system 和 user 消息
    private List<Map<String, String>> buildMessages(String storyInfo, String premise, ModelConfig config) {
        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", buildSystemPrompt(config));
        messages.add(systemMessage);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", buildUserPrompt(storyInfo, premise));
        messages.add(userMessage);

        return messages;
    }

    // 强制模型输出 JSON
    // 强制模型输出 JSON
    private String buildSystemPrompt(ModelConfig config) {
        String providerHint = detectProvider(config);
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位中文短剧剧本编剧助手，同时也是 AI 提示词工程师。");
        prompt.append("请基于用户输入，输出可直接用于生产流程的结构化结果。");
        prompt.append("你必须严格只输出一个 JSON 对象，不要输出 Markdown、解释文字或代码块。");
        prompt.append("JSON 结构必须包含以下字段：storyScript、characterDesign、shotScript、prompt。");
        prompt.append("其中 prompt 应该是适合图像或视频生成的完整提示词，可按场景或镜头分行。");
        prompt.append("如果信息不完整，请根据上下文合理补全，但不要擅自改写核心设定。");
        prompt.append("输出语言保持中文。");
        if ("doubao".equals(providerHint)) {
            prompt.append("你正在面向火山引擎风格的模型接口生成内容，务必保持输出简洁且格式稳定。");
        }
        return prompt.toString();
    }

    // 组织用户输入内容
    // 组织用户输入内容
    private String buildUserPrompt(String storyInfo, String premise) {
        return new StringBuilder()
                .append("【故事信息】\n")
                .append(storyInfo)
                .append("\n\n【前提条件】\n")
                .append(premise)
                .append("\n\n【输出要求】\n")
                .append("请返回如下 JSON：\n")
                .append("{\n")
                .append("  \"storyScript\": \"故事剧本\",\n")
                .append("  \"characterDesign\": \"人物设计\",\n")
                .append("  \"shotScript\": \"分镜脚本\",\n")
                .append("  \"prompt\": \"Prompt 提示词\"\n")
                .append("}\n")
                .append("要求：\n")
                .append("1. 4 个字段都必须存在。\n")
                .append("2. 字段内容不要为空字符串。\n")
                .append("3. prompt 要尽量具体，包含主体、动作、环境、镜头、风格、光影等要素。\n")
                .toString();
    }

    // 按供应商做基础判断
    // 按供应商做基础判断
    private String detectProvider(ModelConfig config) {
        String modelName = config.getModelName() == null ? "" : config.getModelName().toLowerCase(Locale.ROOT);
        String apiUrl = config.getApiUrl() == null ? "" : config.getApiUrl().toLowerCase(Locale.ROOT);
        if (modelName.contains("doubao") || apiUrl.contains("volces")) {
            return "doubao";
        }
        if (modelName.contains("deepseek") || apiUrl.contains("deepseek")) {
            return "deepseek";
        }
        return "generic";
    }

    // 兜底温度参数
    // 兜底温度参数
    private double resolveTemperature(BigDecimal temperature) {
        return temperature == null ? 0.7D : temperature.doubleValue();
    }

    // 提取模型返回内容
    // 提取模型返回内容
    private String extractContent(Map<String, Object> response) {
        if (response == null || !response.containsKey("choices")) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "AI 接口响应格式错误");
        }

        Object choicesObject = response.get("choices");
        if (!(choicesObject instanceof List<?> choices) || choices.isEmpty()) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "AI 接口响应为空");
        }

        Object choiceObject = choices.get(0);
        if (!(choiceObject instanceof Map<?, ?> choiceMap)) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "AI 接口响应格式错误");
        }

        Object messageObject = choiceMap.get("message");
        if (messageObject instanceof Map<?, ?> messageMap && messageMap.get("content") != null) {
            return String.valueOf(messageMap.get("content"));
        }

        Object textObject = choiceMap.get("text");
        if (textObject != null) {
            return String.valueOf(textObject);
        }

        throw new ServiceException(StatusCode.OPERATION_FAILED, "AI 接口缺少内容字段");
    }

    // JSON 优先，旧格式回退
    // JSON 优先，旧格式回退
    private ScriptGenerateResponse parseAIResponse(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "AI 返回内容为空");
        }

        String normalizedContent = normalizeContent(content);
        ScriptGenerateResponse jsonResult = tryParseJson(normalizedContent);
        if (jsonResult != null) {
            jsonResult.setRawContent(content);
            fillMissingFields(jsonResult, normalizedContent);
            return jsonResult;
        }

        ScriptGenerateResponse fallbackResult = parseLegacySections(content);
        fallbackResult.setRawContent(content);
        return fallbackResult;
    }

    // 去掉代码块外壳
    // 去掉代码块外壳
    private String normalizeContent(String content) {
        String trimmed = content.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceFirst("^```(?:json)?\\s*", "");
            trimmed = trimmed.replaceFirst("\\s*```$", "");
        }

        int startIndex = trimmed.indexOf('{');
        int endIndex = trimmed.lastIndexOf('}');
        if (startIndex >= 0 && endIndex > startIndex) {
            return trimmed.substring(startIndex, endIndex + 1);
        }
        return trimmed;
    }

    // 直接按 JSON 反序列化
    // 直接按 JSON 反序列化
    private ScriptGenerateResponse tryParseJson(String content) {
        try {
            return objectMapper.readValue(content, ScriptGenerateResponse.class);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    // 补齐缺失字段
    // 补齐缺失字段
    private void fillMissingFields(ScriptGenerateResponse result, String fallbackContent) {
        if (isBlank(result.getStoryScript()) || isBlank(result.getCharacterDesign())
                || isBlank(result.getShotScript()) || isBlank(result.getPrompt())) {
            ScriptGenerateResponse fallback = parseLegacySections(fallbackContent);
            if (isBlank(result.getStoryScript())) {
                result.setStoryScript(fallback.getStoryScript());
            }
            if (isBlank(result.getCharacterDesign())) {
                result.setCharacterDesign(fallback.getCharacterDesign());
            }
            if (isBlank(result.getShotScript())) {
                result.setShotScript(fallback.getShotScript());
            }
            if (isBlank(result.getPrompt())) {
                result.setPrompt(fallback.getPrompt());
            }
        }
    }

    // 兼容旧的分段文本格式
    // 兼容旧的分段文本格式
    private ScriptGenerateResponse parseLegacySections(String content) {
        ScriptGenerateResponse result = new ScriptGenerateResponse();
        String[] parts = content.split("\\n\\n");
        for (String part : parts) {
            String lowerPart = part.toLowerCase(Locale.ROOT);
            if (part.contains("故事剧本") || lowerPart.contains("story script")) {
                result.setStoryScript(part.trim());
            } else if (part.contains("人物设计") || lowerPart.contains("character design")) {
                result.setCharacterDesign(part.trim());
            } else if (part.contains("分镜脚本") || lowerPart.contains("shot script")) {
                result.setShotScript(part.trim());
            } else if (part.contains("Prompt") || lowerPart.contains("prompt") || part.contains("提示词")) {
                result.setPrompt(part.trim());
            }
        }

        if (isBlank(result.getStoryScript())) {
            result.setStoryScript(content.trim());
        }
        if (isBlank(result.getCharacterDesign())) {
            result.setCharacterDesign("");
        }
        if (isBlank(result.getShotScript())) {
            result.setShotScript("");
        }
        if (isBlank(result.getPrompt())) {
            result.setPrompt("");
        }
        return result;
    }

    // 判空工具
    // 判空工具
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
