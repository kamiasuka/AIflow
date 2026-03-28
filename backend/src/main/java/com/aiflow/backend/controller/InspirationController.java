package com.aiflow.backend.controller;

import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.dto.inspiration.InspirationCreateRequest;
import com.aiflow.backend.model.Inspiration;
import com.aiflow.backend.service.InspirationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inspiration")
public class InspirationController {

    @Autowired
    private InspirationService inspirationService;

    @PostMapping
    public JsonResult createInspiration(@RequestBody InspirationCreateRequest request) {
        Inspiration inspiration = new Inspiration();
        inspiration.setTitle(request.getTitle());
        inspiration.setSourcePlatform(request.getSourcePlatform());
        inspiration.setSourceType(request.getSourceType());
        inspiration.setCategory(request.getCategory());
        inspiration.setTags(request.getTags());
        inspiration.setSummary(request.getSummary());
        inspiration.setHotValue(request.getHotValue());
        inspiration.setStatus("draft");
        return JsonResult.ok(inspirationService.createInspiration(inspiration));
    }

    @GetMapping("/list")
    public JsonResult getInspirationList() {
        return JsonResult.ok(inspirationService.getAllInspirations());
    }

    @PostMapping("/demo-import")
    public JsonResult importDemoInspirations() {
        return JsonResult.ok(inspirationService.importDemoInspirations());
    }

    @PostMapping("/{id}/create-project")
    public JsonResult createProjectFromInspiration(@PathVariable Long id) {
        return JsonResult.ok(inspirationService.createProjectFromInspiration(id));
    }
}
