package com.aiflow.backend.controller;

import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.dto.project.ProjectCreateRequest;
import com.aiflow.backend.model.Project;
import com.aiflow.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public JsonResult createProject(@RequestBody ProjectCreateRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setSourceType(request.getSourceType());
        project.setSourceTitle(request.getSourceTitle());
        project.setSummary(request.getSummary());
        project.setGenreTags(request.getGenreTags());
        project.setStatus("draft");
        return JsonResult.ok(projectService.createProject(project));
    }

    @GetMapping("/list")
    public JsonResult getProjectList() {
        return JsonResult.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public JsonResult getProject(@PathVariable Long id) {
        return JsonResult.ok(projectService.getProjectById(id));
    }
}
