package com.aiflow.backend.service.impl;

import com.aiflow.backend.dao.ScriptDao;
import com.aiflow.backend.model.Script;
import com.aiflow.backend.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private ScriptDao scriptDao;
    @Autowired
    private  CommonService commonService;

    @Override
    public Map<String, Object> generateScript(String storyInfo, String premise, String apiType) {
        // 根据API类型选择不同的AI服务
        try {
            if ("doubao".equals(apiType)) {
                // 调用豆包API
                return commonService.callDoubaoAPI(storyInfo, premise);
            } else if ("deepseek".equals(apiType)) {
                // 调用DeepSeek API
                return commonService.callDeepSeekAPI(storyInfo, premise);
            } else {
                // 默认使用豆包API
                return commonService.callDoubaoAPI(storyInfo, premise);
            }
        } catch (Exception e) {
            // 处理异常，返回错误信息
            Map<String, Object> result = new HashMap<>();
            result.put("error", "生成剧本时发生错误: " + e.getMessage());
            throw new RuntimeException("生成剧本失败", e);
        }
    }
    


    @Override
    public Script saveScript(Script script) {
        // 使用DAO保存剧本到数据库
        scriptDao.save(script);
        return script;
    }

    @Override
    public Script getScriptById(Long id) {
        // 使用DAO从数据库查询剧本
        return scriptDao.getById(id);
    }
}