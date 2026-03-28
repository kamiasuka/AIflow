package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ImageDao;
import com.aiflow.backend.dto.image.ImageWorkflowItem;
import com.aiflow.backend.dto.image.ImageWorkflowRequest;
import com.aiflow.backend.dto.image.ImageWorkflowResponse;
import com.aiflow.backend.model.GenerationTask;
import com.aiflow.backend.model.Image;
import com.aiflow.backend.model.Project;
import com.aiflow.backend.model.Shot;
import com.aiflow.backend.service.ImageService;
import com.aiflow.backend.service.GenerationTaskService;
import com.aiflow.backend.service.ProjectService;
import com.aiflow.backend.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ShotService shotService;

    @Autowired
    private GenerationTaskService generationTaskService;

    @Override
    public List<String> generateImages(String storyScript, int shotCount, String style) {
        if (storyScript == null || storyScript.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "故事脚本不能为空");
        }
        if (shotCount <= 0 || shotCount > 20) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "分镜数量必须在1-20之间");
        }
        if (style == null || style.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "图片风格不能为空");
        }

        try {
            List<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < shotCount; i++) {
                imageUrls.add("https://example.com/image" + (i + 1) + ".jpg");
            }
            return imageUrls;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "生成图片失败: " + e.getMessage());
        }
    }

    @Override
    public Image saveImage(Image image) {
        if (image == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "图片对象不能为空");
        }
        int rows = imageDao.save(image);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "保存图片失败");
        }
        return image;
    }

    @Override
    public Image getImageById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "图片ID不能为空");
        }
        Image image = imageDao.getById(id);
        if (image == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "图片不存在");
        }
        return image;
    }

    @Override
    public List<Image> getImagesByScriptId(Long scriptId) {
        if (scriptId == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "剧本ID不能为空");
        }
        List<Image> images = imageDao.getByScriptId(scriptId);
        if (images == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "查询图片失败");
        }
        return images;
    }

    @Override
    public ImageWorkflowResponse generateImageWorkflow(ImageWorkflowRequest request) {
        if (request == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "请求不能为空");
        }
        if (request.getShotCount() == null || request.getShotCount() <= 0 || request.getShotCount() > 20) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "镜头数量必须在 1 到 20 之间");
        }
        if (request.getStoryScript() == null || request.getStoryScript().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "故事脚本不能为空");
        }

        Project project = ensureProject(request);
        List<Shot> shotList = shotService.buildShots(
                project.getId(),
                request.getScriptId(),
                request.getStoryScript(),
                request.getShotScript(),
                request.getShotCount(),
                request.getStyle()
        );

        List<ImageWorkflowItem> items = new ArrayList<>();
        for (Shot shot : shotList) {
            String imageUrl = buildMockImageUrl(project.getId(), shot.getShotNo());

            GenerationTask task = new GenerationTask();
            task.setProjectId(project.getId());
            task.setShotId(shot.getId());
            task.setTaskType("image");
            task.setProvider("mock");
            task.setModelName("storyboard-image");
            task.setPromptText(shot.getPromptText());
            task.setStatus("success");
            task.setResultUrl(imageUrl);
            task = generationTaskService.createTask(task);

            if (request.getScriptId() != null) {
                Image image = new Image();
                image.setScriptId(request.getScriptId());
                image.setImageUrl(imageUrl);
                image.setShotIndex(shot.getShotNo());
                image.setStyle(shot.getStyleDesc());
                imageDao.save(image);
            }

            ImageWorkflowItem item = new ImageWorkflowItem();
            item.setShot(shot);
            item.setTaskId(task.getId());
            item.setTaskStatus(task.getStatus());
            item.setImageUrl(imageUrl);
            items.add(item);
        }

        ImageWorkflowResponse response = new ImageWorkflowResponse();
        response.setProject(project);
        response.setItems(items);
        return response;
    }

    private Project ensureProject(ImageWorkflowRequest request) {
        if (request.getProjectId() != null) {
            return projectService.getProjectById(request.getProjectId());
        }

        Project project = new Project();
        project.setName(resolveProjectName(request));
        project.setSourceType("manual");
        project.setSourceTitle(request.getSourceTitle());
        project.setSummary(request.getStoryScript());
        project.setGenreTags(request.getGenreTags());
        project.setStatus("draft");
        return projectService.createProject(project);
    }

    private String resolveProjectName(ImageWorkflowRequest request) {
        if (request.getProjectName() != null && !request.getProjectName().trim().isEmpty()) {
            return request.getProjectName().trim();
        }
        return "故事项目-" + System.currentTimeMillis();
    }

    private String buildMockImageUrl(Long projectId, Integer shotNo) {
        return "https://example.com/project-" + projectId + "/shot-" + shotNo + ".jpg";
    }
}
