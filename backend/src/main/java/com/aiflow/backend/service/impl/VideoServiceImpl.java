package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.VideoDao;
import com.aiflow.backend.dto.video.VideoWorkflowRequest;
import com.aiflow.backend.dto.video.VideoWorkflowResponse;
import com.aiflow.backend.model.GenerationTask;
import com.aiflow.backend.model.Video;
import com.aiflow.backend.service.VideoService;
import com.aiflow.backend.service.GenerationTaskService;
import com.aiflow.backend.service.ProjectService;
import com.aiflow.backend.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ShotService shotService;

    @Autowired
    private GenerationTaskService generationTaskService;

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

    @Override
    public VideoWorkflowResponse generateVideoWorkflow(VideoWorkflowRequest request) {
        if (request == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "请求不能为空");
        }
        if (request.getProjectId() == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目 ID 不能为空");
        }
        if (request.getShotId() == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "镜头 ID 不能为空");
        }
        if (request.getVideoLength() == null || request.getVideoLength() <= 0 || request.getVideoLength() > 30) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "视频时长必须在 1 到 30 秒之间");
        }

        projectService.getProjectById(request.getProjectId());
        shotService.getShotById(request.getShotId());

        String motionPrompt = buildMotionPrompt(request);
        String videoUrl = buildMockVideoUrl(request.getProjectId(), request.getShotId());

        GenerationTask task = new GenerationTask();
        task.setProjectId(request.getProjectId());
        task.setShotId(request.getShotId());
        task.setTaskType("video");
        task.setProvider("mock");
        task.setModelName("storyboard-video");
        task.setPromptText(motionPrompt);
        task.setStatus("success");
        task.setResultUrl(videoUrl);
        task = generationTaskService.createTask(task);

        if (request.getScriptId() != null) {
            Video video = new Video();
            video.setScriptId(request.getScriptId());
            video.setVideoUrl(videoUrl);
            video.setLength(request.getVideoLength());
            video.setStatus("success");
            videoDao.save(video);
        }

        VideoWorkflowResponse response = new VideoWorkflowResponse();
        response.setTaskId(task.getId());
        response.setTaskStatus(task.getStatus());
        response.setVideoUrl(videoUrl);
        return response;
    }

    // 运动提示词单独建模，后续接真实视频模型时可以直接复用。
    private String buildMotionPrompt(VideoWorkflowRequest request) {
        String prompt = request.getMotionPrompt();
        if (prompt == null || prompt.trim().isEmpty()) {
            prompt = "主体缓慢运动，镜头稳定推进，保持人物一致性";
        }
        String cameraMotion = request.getCameraMotion();
        if (cameraMotion != null && !cameraMotion.trim().isEmpty()) {
            prompt = prompt.trim() + "；镜头运动：" + cameraMotion.trim();
        }
        return prompt;
    }

    private String buildMockVideoUrl(Long projectId, Long shotId) {
        return "https://example.com/project-" + projectId + "/shot-" + shotId + ".mp4";
    }
}
