package com.aiflow.backend.dao;

import com.aiflow.backend.model.Shot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShotDao {
    int insert(Shot shot);

    int insertBatch(List<Shot> shotList);

    Shot getById(Long id);

    List<Shot> findByProjectId(Long projectId);

    int deleteByProjectId(Long projectId);

    int update(Shot shot);
}
