package com.aiflow.backend.dao;

import com.aiflow.backend.model.ModelConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 大模型配置数据访问接口
 */
@Mapper
public interface ModelConfigDao {
    /**
     * 查询所有配置
     */
    List<ModelConfig> findAll();

    /**
     * 根据ID查询配置
     */
    ModelConfig getById(@Param("id") Long id);

    /**
     * 根据模型名称查询配置
     */
    List<ModelConfig> findByModelName(@Param("modelName") String modelName);

    /**
     * 查询启用的配置
     */
    List<ModelConfig> findEnabled();

    /**
     * 查询默认配置
     */
    ModelConfig findDefault();

    /**
     * 插入配置
     */
    int insert(ModelConfig modelConfig);

    /**
     * 更新配置
     */
    int update(ModelConfig modelConfig);

    /**
     * 删除配置
     */
    int deleteById(@Param("id") Long id);

    /**
     * 清除默认配置
     */
    int clearDefault();

    /**
     * 设置默认配置
     */
    int setDefault(@Param("id") Long id);
}
