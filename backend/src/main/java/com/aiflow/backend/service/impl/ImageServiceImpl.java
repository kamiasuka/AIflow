package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ImageDao;
import com.aiflow.backend.model.Image;
import com.aiflow.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    public List<String> generateImages(String storyScript, int shotCount, String style) {
        if (storyScript == null || storyScript.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "故事脚本不能为空");
        }
        if (shotCount <= 0 || shotCount > 20) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "分镜数量必须在1-20之间");
        }
        if (style == null || style.trim().isEmpty()) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "图片风格不能为空");
        }

        try {
            List<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < shotCount; i++) {
                imageUrls.add("https://example.com/image" + (i + 1) + ".jpg");
            }
            return imageUrls;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "生成图片失败: " + e.getMessage());
        }
    }

    @Override
    public Image saveImage(Image image) {
        if (image == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "图片对象不能为空");
        }
        int rows = imageDao.save(image);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "保存图片失败");
        }
        return image;
    }

    @Override
    public Image getImageById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "图片ID不能为空");
        }
        Image image = imageDao.getById(id);
        if (image == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "图片不存在");
        }
        return image;
    }

    @Override
    public List<Image> getImagesByScriptId(Long scriptId) {
        if (scriptId == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "剧本ID不能为空");
        }
        List<Image> images = imageDao.getByScriptId(scriptId);
        if (images == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "查询图片失败");
        }
        return images;
    }
}
