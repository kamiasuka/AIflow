package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.VideoDao;
import com.aiflow.backend.model.Video;
import com.aiflow.backend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;

    @Override
    public String generateVideo(String script, int videoLength) {
        if (script == null || script.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "剧本内容不能为空");
        }
        if (videoLength <= 0 || videoLength > 300) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "视频长度必须在1-300秒之间");
        }

        try {
            return "https://example.com/sample-video.mp4";
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "生成视频失败: " + e.getMessage());
        }
    }

    @Override
    public Video saveVideo(Video video) {
        if (video == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "视频对象不能为空");
        }
        int rows = videoDao.save(video);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "保存视频失败");
        }
        return video;
    }

    @Override
    public Video getVideoById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "视频ID不能为空");
        }
        Video video = videoDao.getById(id);
        if (video == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "视频不存在");
        }
        return video;
    }

    @Override
    public Video getVideoByScriptId(Long scriptId) {
        if (scriptId == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "剧本ID不能为空");
        }
        Video video = videoDao.getByScriptId(scriptId);
        if (video == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "视频不存在");
        }
        return video;
    }
}
