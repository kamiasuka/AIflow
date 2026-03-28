package com.aiflow.backend.dao;

import com.aiflow.backend.model.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectDao {
    int insert(Project project);

    Project getById(Long id);

    List<Project> findAll();

    int update(Project project);
}
