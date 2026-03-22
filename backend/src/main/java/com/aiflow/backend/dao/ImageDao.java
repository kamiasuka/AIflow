package com.aiflow.backend.dao;

import com.aiflow.backend.model.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 图片DAO接口
 * 用于图片相关的数据库操作
 */
@Mapper
public interface ImageDao {
    /**
     * 保存图片
     * @param image 图片对象
     * @return 保存成功返回1，失败返回0
     */
    int save(Image image);

    /**
     * 根据ID获取图片
     * @param id 图片ID
     * @return 图片对象
     */
    Image getById(Long id);

    /**
     * 根据剧本ID获取图片列表
     * @param scriptId 剧本ID
     * @return 图片列表
     */
    List<Image> getByScriptId(Long scriptId);

    /**
     * 更新图片
     * @param image 图片对象
     * @return 更新成功返回1，失败返回0
     */
    int update(Image image);

    /**
     * 删除图片
     * @param id 图片ID
     * @return 删除成功返回1，失败返回0
     */
    int delete(Long id);

    /**
     * 根据剧本ID删除图片
     * @param scriptId 剧本ID
     * @return 删除成功返回影响的行数
     */
    int deleteByScriptId(Long scriptId);
}
