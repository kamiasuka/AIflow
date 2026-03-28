package com.aiflow.backend.service.impl;

import com.aiflow.backend.common.exception.ServiceException;
import com.aiflow.backend.common.response.StatusCode;
import com.aiflow.backend.dao.InspirationDao;
import com.aiflow.backend.model.Inspiration;
import com.aiflow.backend.model.Project;
import com.aiflow.backend.service.InspirationService;
import com.aiflow.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InspirationServiceImpl implements InspirationService {

    @Autowired
    private InspirationDao inspirationDao;

    @Autowired
    private ProjectService projectService;

    @Override
    public Inspiration createInspiration(Inspiration inspiration) {
        if (inspiration == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "灵感不能为空");
        }
        if (isBlank(inspiration.getTitle())) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "灵感标题不能为空");
        }

        inspiration.setTitle(inspiration.getTitle().trim());
        if (isBlank(inspiration.getSourceType())) {
            inspiration.setSourceType("manual");
        }
        if (isBlank(inspiration.getStatus())) {
            inspiration.setStatus("draft");
        }

        int rows = inspirationDao.insert(inspiration);
        if (rows <= 0) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "创建灵感失败");
        }
        return inspirationDao.getById(inspiration.getId());
    }

    @Override
    public List<Inspiration> getAllInspirations() {
        return inspirationDao.findAll();
    }

    @Override
    public Inspiration getInspirationById(Long id) {
        if (id == null) {
            throw new ServiceException(StatusCode.VALIDATED_ERROR, "灵感 ID 不能为空");
        }
        Inspiration inspiration = inspirationDao.getById(id);
        if (inspiration == null) {
            throw new ServiceException(StatusCode.OPERATION_FAILED, "灵感不存在");
        }
        return inspiration;
    }

    @Override
    public List<Inspiration> importDemoInspirations() {
        List<Inspiration> result = new ArrayList<>();
        result.add(createIfNeeded("末世小队生存", "番茄小说", "热榜采样", "末世,团队,求生", "高热"));
        result.add(createIfNeeded("规则怪谈校园局", "起点中文网", "热榜采样", "规则怪谈,校园,悬疑", "高热"));
        result.add(createIfNeeded("都市异能逆袭", "七猫小说", "热榜采样", "都市,异能,逆袭", "上升"));
        return result;
    }

    @Override
    public Project createProjectFromInspiration(Long inspirationId) {
        Inspiration inspiration = getInspirationById(inspirationId);

        Project project = new Project();
        project.setName(inspiration.getTitle());
        project.setSourceType("inspiration");
        project.setSourceTitle(inspiration.getSourcePlatform());
        project.setGenreTags(inspiration.getTags());
        project.setSummary(inspiration.getSummary());
        project.setStatus("draft");
        return projectService.createProject(project);
    }

    private Inspiration createIfNeeded(String title, String platform, String category, String tags, String hotValue) {
        for (Inspiration item : inspirationDao.findAll()) {
            if (title.equals(item.getTitle()) && platform.equals(item.getSourcePlatform())) {
                return item;
            }
        }

        Inspiration inspiration = new Inspiration();
        inspiration.setTitle(title);
        inspiration.setSourcePlatform(platform);
        inspiration.setSourceType("demo");
        inspiration.setCategory(category);
        inspiration.setTags(tags);
        inspiration.setSummary("适合改编成短剧脚本，具备强冲突、强反转和明确的人物目标。");
        inspiration.setHotValue(hotValue);
        inspiration.setStatus("draft");
        return createInspiration(inspiration);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
