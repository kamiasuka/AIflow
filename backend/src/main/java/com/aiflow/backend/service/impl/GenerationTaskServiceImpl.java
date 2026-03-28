package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.GenerationTaskDao;
import com.aiflow.backend.model.GenerationTask;
import com.aiflow.backend.service.GenerationTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerationTaskServiceImpl implements GenerationTaskService {

    @Autowired
    private GenerationTaskDao generationTaskDao;

    @Override
    public GenerationTask createTask(GenerationTask task) {
        if (task == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "任务不能为空");
        }
        if (task.getProjectId() == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目 ID 不能为空");
        }
        if (task.getTaskType() == null || task.getTaskType().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "任务类型不能为空");
        }

        if (task.getProvider() == null || task.getProvider().trim().isEmpty()) {
            task.setProvider("mock");
        }
        if (task.getStatus() == null || task.getStatus().trim().isEmpty()) {
            task.setStatus("success");
        }

        int rows = generationTaskDao.insert(task);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "创建任务失败");
        }
        return generationTaskDao.getById(task.getId());
    }

    @Override
    public GenerationTask getTaskById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "任务 ID 不能为空");
        }
        GenerationTask task = generationTaskDao.getById(id);
        if (task == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "任务不存在");
        }
        return task;
    }

    @Override
    public List<GenerationTask> getTasksByProjectId(Long projectId) {
        if (projectId == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目 ID 不能为空");
        }
        return generationTaskDao.findByProjectId(projectId);
    }
}
