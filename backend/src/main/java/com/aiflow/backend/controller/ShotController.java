package com.aiflow.backend.controller;

import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.dto.shot.ShotParseRequest;
import com.aiflow.backend.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shot")
public class ShotController {

    @Autowired
    private ShotService shotService;

    @PostMapping("/parse")
    public JsonResult parseShots(@RequestBody ShotParseRequest request) {
        return JsonResult.ok(shotService.buildShots(
                request.getProjectId(),
                request.getScriptId(),
                request.getStoryScript(),
                request.getShotScript(),
                request.getShotCount(),
                request.getStyle()
        ));
    }

    @GetMapping("/project/{projectId}")
    public JsonResult getShotsByProjectId(@PathVariable Long projectId) {
        return JsonResult.ok(shotService.getShotsByProjectId(projectId));
    }
}
