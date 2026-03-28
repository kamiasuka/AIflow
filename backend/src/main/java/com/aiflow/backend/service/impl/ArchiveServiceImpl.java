package com.aiflow.backend.service.impl;

import com.aiflow.backend.dto.archive.ArchiveProjectDetail;
import com.aiflow.backend.dto.archive.ArchiveProjectItem;
import com.aiflow.backend.dto.archive.ArchiveShotItem;
import com.aiflow.backend.model.GenerationTask;
import com.aiflow.backend.model.Project;
import com.aiflow.backend.model.Shot;
import com.aiflow.backend.service.ArchiveService;
import com.aiflow.backend.service.GenerationTaskService;
import com.aiflow.backend.service.ProjectService;
import com.aiflow.backend.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ShotService shotService;

    @Autowired
    private GenerationTaskService generationTaskService;

    @Override
    public List<ArchiveProjectItem> getProjectArchiveList() {
        List<Project> projectList = projectService.getAllProjects();
        List<ArchiveProjectItem> result = new ArrayList<>();
        for (Project project : projectList) {
            List<Shot> shotList = shotService.getShotsByProjectId(project.getId());
            List<GenerationTask> taskList = generationTaskService.getTasksByProjectId(project.getId());

            ArchiveProjectItem item = new ArchiveProjectItem();
            item.setId(project.getId());
            item.setName(project.getName());
            item.setSourceType(project.getSourceType());
            item.setSourceTitle(project.getSourceTitle());
            item.setGenreTags(project.getGenreTags());
            item.setStatus(project.getStatus());
            item.setShotCount(shotList.size());
            item.setImageTaskCount(countTasks(taskList, "image"));
            item.setVideoTaskCount(countTasks(taskList, "video"));
            result.add(item);
        }
        return result;
    }

    @Override
    public ArchiveProjectDetail getProjectArchiveDetail(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        List<Shot> shotList = shotService.getShotsByProjectId(projectId);
        List<GenerationTask> taskList = generationTaskService.getTasksByProjectId(projectId);

        ArchiveProjectDetail detail = new ArchiveProjectDetail();
        detail.setProject(project);
        detail.setShotList(buildShotItems(shotList, taskList));
        detail.setTaskList(taskList);
        return detail;
    }

    // 用任务结果回填素材地址，保持前后端结构稳定。
    private List<ArchiveShotItem> buildShotItems(List<Shot> shotList, List<GenerationTask> taskList) {
        List<ArchiveShotItem> result = new ArrayList<>();
        for (Shot shot : shotList) {
            ArchiveShotItem item = new ArchiveShotItem();
            item.setShotId(shot.getId());
            item.setShotNo(shot.getShotNo());
            item.setSceneTitle(shot.getSceneTitle());
            item.setActionDesc(shot.getActionDesc());
            item.setPromptText(shot.getPromptText());
            item.setImageStatus(shot.getImageStatus());
            item.setVideoStatus(shot.getVideoStatus());
            item.setImageUrl(findLatestTaskUrl(taskList, shot.getId(), "image"));
            item.setVideoUrl(findLatestTaskUrl(taskList, shot.getId(), "video"));
            result.add(item);
        }
        return result;
    }

    private int countTasks(List<GenerationTask> taskList, String taskType) {
        int count = 0;
        for (GenerationTask task : taskList) {
            if (taskType.equals(task.getTaskType())) {
                count += 1;
            }
        }
        return count;
    }

    private String findLatestTaskUrl(List<GenerationTask> taskList, Long shotId, String taskType) {
        for (GenerationTask task : taskList) {
            if (taskType.equals(task.getTaskType()) && shotId != null && shotId.equals(task.getShotId())) {
                return task.getResultUrl();
            }
        }
        return "";
    }
}
