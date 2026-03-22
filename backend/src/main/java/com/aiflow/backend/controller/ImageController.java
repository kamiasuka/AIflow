package com.aiflow.backend.controller;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.model.Image;
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
     * @return 统一的JSON返回结果
     */
    @PostMapping("/generate")
    public JsonResult generateImages(@RequestBody Map<String, Object> request) {
        String storyScript = getRequiredString(request, "storyScript", "故事脚本");
        int shotCount = getRequiredInt(request, "shotCount", "分镜数量");
        String style = getRequiredString(request, "style", "图片风格");

        List<String> imageUrls = imageService.generateImages(storyScript, shotCount, style);
        return JsonResult.ok(imageUrls);
    }

    /**
     * 根据剧本ID获取图片列表
     * @param scriptId 剧本ID
     * @return 统一的JSON返回结果
     */
    @GetMapping("/script/{scriptId}")
    public JsonResult getImagesByScriptId(@PathVariable Long scriptId) {
        List<Image> images = imageService.getImagesByScriptId(scriptId);
        List<String> imageUrls = images.stream()
                .map(image -> image.getImageUrl())
                .toList();
        return JsonResult.ok(imageUrls);
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
