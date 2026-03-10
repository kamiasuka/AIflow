package com.aiflow.backend.service;

import com.aiflow.backend.model.Image;

import java.util.List;

/**
 * 图片服务接口
 * 处理图片相关的业务逻辑
 */
public interface ImageService {
    /**
     * 生成图片
     * @param storyScript 故事脚本
     * @param shotCount 分镜数量
     * @param style 图片风格
     * @return 生成的图片URL列表
     */
    List<String> generateImages(String storyScript, int shotCount, String style);

    /**
     * 保存图片
     * @param image 图片对象
     * @return 保存后的图片对象
     */
    Image saveImage(Image image);

    /**
     * 根据ID获取图片
     * @param id 图片ID
     * @return 图片对象
     */
    Image getImageById(Long id);

    /**
     * 根据剧本ID获取图片列表
     * @param scriptId 剧本ID
     * @return 图片列表
     */
    List<Image> getImagesByScriptId(Long scriptId);
}
