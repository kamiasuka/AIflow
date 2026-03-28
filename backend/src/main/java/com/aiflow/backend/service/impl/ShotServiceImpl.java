package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.ShotDao;
import com.aiflow.backend.model.Shot;
import com.aiflow.backend.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShotServiceImpl implements ShotService {

    @Autowired
    private ShotDao shotDao;

    @Override
    public List<Shot> buildShots(Long projectId, Long scriptId, String storyScript, String shotScript, Integer shotCount, String style) {
        if (projectId == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目 ID 不能为空");
        }
        if (shotCount == null || shotCount <= 0 || shotCount > 20) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "镜头数量必须在 1 到 20 之间");
        }

        List<String> shotBlocks = splitShotBlocks(shotScript, storyScript, shotCount);
        List<Shot> shotList = new ArrayList<>();
        for (int i = 0; i < shotBlocks.size(); i++) {
            String block = shotBlocks.get(i);
            Shot shot = new Shot();
            shot.setProjectId(projectId);
            shot.setScriptId(scriptId);
            shot.setShotNo(i + 1);
            shot.setSceneTitle("镜头 " + (i + 1));
            shot.setSubject(extractSummary(block, 24));
            shot.setActionDesc(block);
            shot.setEnvDesc(extractSummary(storyScript, 60));
            shot.setCameraDesc("中景");
            shot.setStyleDesc(defaultText(style, "影视感"));
            shot.setDurationSec(5);
            shot.setPromptText(buildPrompt(block, storyScript, style, i + 1));
            shot.setNegativePrompt("低质量，模糊，畸形，多余肢体，文字水印");
            shot.setImageStatus("pending");
            shot.setVideoStatus("pending");
            shotList.add(shot);
        }

        shotDao.deleteByProjectId(projectId);
        if (!shotList.isEmpty()) {
            shotDao.insertBatch(shotList);
        }
        return shotDao.findByProjectId(projectId);
    }

    @Override
    public List<Shot> getShotsByProjectId(Long projectId) {
        if (projectId == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "项目 ID 不能为空");
        }
        return shotDao.findByProjectId(projectId);
    }

    @Override
    public Shot getShotById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "镜头 ID 不能为空");
        }
        Shot shot = shotDao.getById(id);
        if (shot == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "镜头不存在");
        }
        return shot;
    }

    private List<String> splitShotBlocks(String shotScript, String storyScript, Integer shotCount) {
        List<String> blocks = new ArrayList<>();
        String normalized = defaultText(shotScript, storyScript).replace("\r", "");
        for (String part : normalized.split("\n\n")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                blocks.add(trimmed);
            }
        }
        if (blocks.isEmpty()) {
            for (String line : normalized.split("\n")) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    blocks.add(trimmed);
                }
            }
        }
        while (blocks.size() < shotCount) {
            int index = blocks.size() + 1;
            blocks.add("围绕故事主线补充镜头 " + index + "，突出关键角色和情绪变化");
        }
        if (blocks.size() > shotCount) {
            return blocks.subList(0, shotCount);
        }
        return blocks;
    }

    // 用统一模板生成图片提示词，方便后续替换供应商适配器。
    private String buildPrompt(String shotBlock, String storyScript, String style, int shotNo) {
        return "镜头" + shotNo
                + "，主体：" + extractSummary(shotBlock, 40)
                + "；环境：" + extractSummary(storyScript, 50)
                + "；风格：" + defaultText(style, "影视感")
                + "；要求：人物一致性，构图清晰，电影级光影，细节丰富。";
    }

    private String extractSummary(String value, int maxLen) {
        String text = defaultText(value, "");
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen);
    }

    private String defaultText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
}
