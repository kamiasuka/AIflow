package com.aiflow.backend.controller;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dto.video.VideoWorkflowRequest;
import com.aiflow.backend.model.Video;
import com.aiflow.backend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 视频控制器
 * 处理视频相关的HTTP请求
 */
@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 生成视频
     * @param request 请求参数，包含剧本信息和视频长度
     * @return 统一的JSON返回结果
     */
    @PostMapping("/generate")
    public JsonResult generateVideo(@RequestBody Map<String, Object> request) {
        String script = getRequiredString(request, "script", "剧本内容");
        int videoLength = getRequiredInt(request, "videoLength", "视频长度");

        String videoUrl = videoService.generateVideo(script, videoLength);
        return JsonResult.ok(videoUrl);
    }

    @PostMapping("/workflow/generate")
    public JsonResult generateVideoWorkflow(@RequestBody VideoWorkflowRequest request) {
        return JsonResult.ok(videoService.generateVideoWorkflow(request));
    }

    /**
     * 根据剧本ID获取视频
     * @param scriptId 剧本ID
     * @return 统一的JSON返回结果
     */
    @GetMapping("/script/{scriptId}")
    public JsonResult getVideoByScriptId(@PathVariable Long scriptId) {
        Video video = videoService.getVideoByScriptId(scriptId);
        return JsonResult.ok(video.getVideoUrl());
    }

    private String getRequiredString(Map<String, Object> request, String key, String fieldName) {
        Object value = request.get(key);
        if (!(value instanceof String strValue) || strValue.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, fieldName + "不能为空");
        }
        return strValue.trim();
    }

    private int getRequiredInt(Map<String, Object> request, String key, String fieldName) {
        Object value = request.get(key);
        if (value == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, fieldName + "不能为空");
        }
        if (value instanceof Number numberValue) {
            return numberValue.intValue();
        }
        if (value instanceof String strValue) {
            try {
                return Integer.parseInt(strValue.trim());
            } catch (NumberFormatException ex) {
                throw new ServiceException(StatusCode.VALIDATED_ERROR, fieldName + "格式不正确");
            }
        }
        throw new ServiceException(StatusCode.VALIDATED_ERROR, fieldName + "格式不正确");
    }
}
