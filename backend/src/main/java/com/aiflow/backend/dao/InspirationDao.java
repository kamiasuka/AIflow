package com.aiflow.backend.dao;

import com.aiflow.backend.model.Inspiration;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InspirationDao {
    int insert(Inspiration inspiration);

    Inspiration getById(Long id);

    List<Inspiration> findAll();
}
