package com.aiflow.backend.dao;

import com.aiflow.backend.model.Script;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScriptDao {
    // 保存剧本
    int save(Script script);

    // 按 ID 查询剧本
    Script getById(Long id);

    // 更新剧本
    int update(Script script);

    // 删除剧本
    int delete(Long id);
}