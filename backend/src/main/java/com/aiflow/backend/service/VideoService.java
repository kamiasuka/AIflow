package com.aiflow.backend.service;

import com.aiflow.backend.model.Video;

/**
 * 视频服务接口
 * 处理视频相关的业务逻辑
 */
public interface VideoService {
    /**
     * 生成视频
     * @param script 剧本信息
     * @param videoLength 视频长度
     * @return 生成的视频URL
     */
    String generateVideo(String script, int videoLength);

    /**
     * 保存视频
     * @param video 视频对象
     * @return 保存后的视频对象
     */
    Video saveVideo(Video video);

    /**
     * 根据ID获取视频
     * @param id 视频ID
     * @return 视频对象
     */
    Video getVideoById(Long id);

    /**
     * 根据剧本ID获取视频
     * @param scriptId 剧本ID
     * @return 视频对象
     */
    Video getVideoByScriptId(Long scriptId);
}
