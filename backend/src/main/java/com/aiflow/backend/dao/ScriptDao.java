package com.aiflow.backend.dao;

import com.aiflow.backend.model.Script;

/**
 * 剧本DAO接口
 * 用于剧本相关的数据库操作
 */
public interface ScriptDao {
    /**
     * 保存剧本
     * @param script 剧本对象
     * @return 保存成功返回1，失败返回0
     */
    int save(Script script);

    /**
     * 根据ID获取剧本
     * @param id 剧本ID
     * @return 剧本对象
     */
    Script getById(Long id);

    /**
     * 更新剧本
     * @param script 剧本对象
     * @return 更新成功返回1，失败返回0
     */
    int update(Script script);

    /**
     * 删除剧本
     * @param id 剧本ID
     * @return 删除成功返回1，失败返回0
     */
    int delete(Long id);
}