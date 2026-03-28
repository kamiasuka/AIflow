package com.aiflow.backend.service;

import com.aiflow.backend.model.Shot;

import java.util.List;

public interface ShotService {
    List<Shot> buildShots(Long projectId, Long scriptId, String storyScript, String shotScript, Integer shotCount, String style);

    List<Shot> getShotsByProjectId(Long projectId);

    Shot getShotById(Long id);
}
