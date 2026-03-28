package com.aiflow.backend.service;

import com.aiflow.backend.dto.archive.ArchiveProjectDetail;
import com.aiflow.backend.dto.archive.ArchiveProjectItem;

import java.util.List;

public interface ArchiveService {
    List<ArchiveProjectItem> getProjectArchiveList();

    ArchiveProjectDetail getProjectArchiveDetail(Long projectId);
}
