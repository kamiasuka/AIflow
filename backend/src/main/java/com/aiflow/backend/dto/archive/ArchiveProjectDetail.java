package com.aiflow.backend.dto.archive;

import com.aiflow.backend.model.GenerationTask;
import com.aiflow.backend.model.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ArchiveProjectDetail {
    private Project project;
    private List<ArchiveShotItem> shotList;
    private List<GenerationTask> taskList;

}
