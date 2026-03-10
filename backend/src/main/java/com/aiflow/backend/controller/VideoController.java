package com.aiflow.backend.controller;

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
     * @return 生成的视频URL
     */
    @PostMapping("/generate")
    public String generateVideo(@RequestBody Map<String, Object> request) {
        String script = (String) request.get("script");
        int videoLength = (int) request.get("videoLength");
        return videoService.generateVideo(script, videoLength);
    }

    /**
     * 根据剧本ID获取视频
     * @param scriptId 剧本ID
     * @return 视频URL
     */
    @GetMapping("/script/{scriptId}")
    public String getVideoByScriptId(@PathVariable Long scriptId) {
        return videoService.getVideoByScriptId(scriptId).getVideoUrl();
    }
}
