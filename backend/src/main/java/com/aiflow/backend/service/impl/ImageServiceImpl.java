package com.aiflow.backend.service.impl;

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
        // 模拟API调用，返回模拟数据
        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < shotCount; i++) {
            imageUrls.add("https://example.com/image" + (i + 1) + ".jpg");
        }
        return imageUrls;
    }

    @Override
    public Image saveImage(Image image) {
        // 使用DAO保存图片
        imageDao.save(image);
        return image;
    }

    @Override
    public Image getImageById(Long id) {
        // 使用DAO获取图片
        return imageDao.getById(id);
    }

    @Override
    public List<Image> getImagesByScriptId(Long scriptId) {
        // 使用DAO获取图片列表
        return imageDao.getByScriptId(scriptId);
    }
}
