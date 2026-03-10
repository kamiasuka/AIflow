package com.aiflow.backend.dao;

import com.aiflow.backend.model.Video;

/**
 * 视频DAO接口
 * 用于视频相关的数据库操作
 */
public interface VideoDao {
    /**
     * 保存视频
     * @param video 视频对象
     * @return 保存成功返回1，失败返回0
     */
    int save(Video video);

    /**
     * 根据ID获取视频
     * @param id 视频ID
     * @return 视频对象
     */
    Video getById(Long id);

    /**
     * 根据剧本ID获取视频
     * @param scriptId 剧本ID
     * @return 视频对象
     */
    Video getByScriptId(Long scriptId);

    /**
     * 更新视频
     * @param video 视频对象
     * @return 更新成功返回1，失败返回0
     */
    int update(Video video);

    /**
     * 删除视频
     * @param id 视频ID
     * @return 删除成功返回1，失败返回0
     */
    int delete(Long id);

    /**
     * 根据剧本ID删除视频
     * @param scriptId 剧本ID
     * @return 删除成功返回影响的行数
     */
    int deleteByScriptId(Long scriptId);
}