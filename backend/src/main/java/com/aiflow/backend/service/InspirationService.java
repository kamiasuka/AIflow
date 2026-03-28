package com.aiflow.backend.service;

import com.aiflow.backend.model.Inspiration;
import com.aiflow.backend.model.Project;

import java.util.List;

public interface InspirationService {
    Inspiration createInspiration(Inspiration inspiration);

    List<Inspiration> getAllInspirations();

    Inspiration getInspirationById(Long id);

    List<Inspiration> importDemoInspirations();

    Project createProjectFromInspiration(Long inspirationId);
}
