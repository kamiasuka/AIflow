package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ProjectDao;
import com.aiflow.backend.model.Project;
import com.aiflow.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Project createProject(Project project) {
        if (project == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目不能为空");
        }
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目名称不能为空");
        }

        // 补齐默认字段，避免前端反复传递样板值。
        project.setName(project.getName().trim());
        if (project.getSourceType() == null || project.getSourceType().trim().isEmpty()) {
            project.setSourceType("manual");
        }
        if (project.getStatus() == null || project.getStatus().trim().isEmpty()) {
            project.setStatus("draft");
        }

        int rows = projectDao.insert(project);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "创建项目失败");
        }
        return projectDao.getById(project.getId());
    }

    @Override
    public Project getProjectById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目 ID 不能为空");
        }
        Project project = projectDao.getById(id);
        if (project == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "项目不存在");
        }
        return project;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectDao.findAll();
    }
}
