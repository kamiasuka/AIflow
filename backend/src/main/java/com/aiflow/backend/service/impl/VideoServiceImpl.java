package com.aiflow.backend.service.impl;

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
        // 模拟API调用，返回模拟数据
        return "https://example.com/sample-video.mp4";
    }

    @Override
    public Video saveVideo(Video video) {
        // 使用DAO保存视频
        videoDao.save(video);
        return video;
    }

    @Override
    public Video getVideoById(Long id) {
        // 使用DAO获取视频
        return videoDao.getById(id);
    }

    @Override
    public Video getVideoByScriptId(Long scriptId) {
        // 使用DAO获取视频
        return videoDao.getByScriptId(scriptId);
    }
}
