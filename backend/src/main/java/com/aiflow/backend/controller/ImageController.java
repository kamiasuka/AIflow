package com.aiflow.backend.controller;

import com.aiflow.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 图片控制器
 * 处理图片相关的HTTP请求
 */
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 生成图片
     * @param request 请求参数，包含故事脚本、分镜数量和风格
     * @return 生成的图片URL列表
     */
    @PostMapping("/generate")
    public List<String> generateImages(@RequestBody Map<String, Object> request) {
        String storyScript = (String) request.get("storyScript");
        int shotCount = (int) request.get("shotCount");
        String style = (String) request.get("style");
        return imageService.generateImages(storyScript, shotCount, style);
    }

    /**
     * 根据剧本ID获取图片列表
     * @param scriptId 剧本ID
     * @return 图片URL列表
     */
    @GetMapping("/script/{scriptId}")
    public List<String> getImagesByScriptId(@PathVariable Long scriptId) {
        return imageService.getImagesByScriptId(scriptId).stream()
                .map(image -> image.getImageUrl())
                .toList();
    }
}
