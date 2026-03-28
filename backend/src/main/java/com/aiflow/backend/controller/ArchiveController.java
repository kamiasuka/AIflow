package com.aiflow.backend.controller;

import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/archive")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @GetMapping("/project/list")
    public JsonResult getProjectArchiveList() {
        return JsonResult.ok(archiveService.getProjectArchiveList());
    }

    @GetMapping("/project/{projectId}")
    public JsonResult getProjectArchiveDetail(@PathVariable Long projectId) {
        return JsonResult.ok(archiveService.getProjectArchiveDetail(projectId));
    }
}
