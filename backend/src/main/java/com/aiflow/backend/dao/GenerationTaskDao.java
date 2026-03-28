package com.aiflow.backend.dao;

import com.aiflow.backend.model.GenerationTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GenerationTaskDao {
    int insert(GenerationTask task);

    GenerationTask getById(Long id);

    List<GenerationTask> findByProjectId(Long projectId);

    List<GenerationTask> findByShotId(Long shotId);

    int update(GenerationTask task);
}
