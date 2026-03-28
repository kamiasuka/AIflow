CREATE TABLE IF NOT EXISTS model_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置 ID',
    model_name VARCHAR(100) NOT NULL COMMENT '模型名称',
    api_url VARCHAR(500) NOT NULL COMMENT 'API 地址',
    api_key VARCHAR(255) NOT NULL COMMENT 'API Key',
    model_version VARCHAR(100) COMMENT '模型版本',
    max_tokens INT DEFAULT 4096 COMMENT '最大 token 数',
    temperature DECIMAL(3,2) DEFAULT 0.70 COMMENT '温度参数',
    is_enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认',
    description VARCHAR(500) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT uk_model_config_name UNIQUE (model_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模型配置表';

CREATE TABLE IF NOT EXISTS project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目 ID',
    name VARCHAR(200) NOT NULL COMMENT '项目名称',
    source_type VARCHAR(50) DEFAULT 'manual' COMMENT '项目来源',
    source_title VARCHAR(255) COMMENT '来源标题',
    summary TEXT COMMENT '故事概括',
    genre_tags VARCHAR(255) COMMENT '题材标签',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '项目状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

CREATE TABLE IF NOT EXISTS inspiration (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '灵感 ID',
    title VARCHAR(255) NOT NULL COMMENT '标题',
    source_platform VARCHAR(100) COMMENT '来源平台',
    source_type VARCHAR(50) DEFAULT 'manual' COMMENT '来源类型',
    category VARCHAR(100) COMMENT '题材分类',
    tags VARCHAR(255) COMMENT '标签',
    summary TEXT COMMENT '简介摘要',
    hot_value VARCHAR(50) COMMENT '热度描述',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='灵感表';

CREATE TABLE IF NOT EXISTS script (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '剧本 ID',
    story_info TEXT COMMENT '故事信息',
    premise TEXT COMMENT '前提条件',
    model_config_id BIGINT COMMENT '使用的模型配置 ID',
    story_script TEXT COMMENT '故事剧本',
    character_design TEXT COMMENT '人物设计',
    shot_script TEXT COMMENT '分镜脚本',
    prompt TEXT COMMENT 'Prompt 提示词',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_script_model_config FOREIGN KEY (model_config_id) REFERENCES model_config(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='剧本表';

CREATE TABLE IF NOT EXISTS shot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '镜头 ID',
    project_id BIGINT NOT NULL COMMENT '所属项目 ID',
    script_id BIGINT COMMENT '关联剧本 ID',
    shot_no INT NOT NULL COMMENT '镜头序号',
    scene_title VARCHAR(200) COMMENT '场景标题',
    subject VARCHAR(255) COMMENT '主体描述',
    action_desc VARCHAR(500) COMMENT '动作描述',
    env_desc VARCHAR(500) COMMENT '环境描述',
    camera_desc VARCHAR(255) COMMENT '镜头描述',
    style_desc VARCHAR(255) COMMENT '风格描述',
    duration_sec INT DEFAULT 5 COMMENT '建议时长',
    prompt_text TEXT COMMENT '图片提示词',
    negative_prompt TEXT COMMENT '负向提示词',
    image_status VARCHAR(20) DEFAULT 'pending' COMMENT '图片状态',
    video_status VARCHAR(20) DEFAULT 'pending' COMMENT '视频状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_shot_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT fk_shot_script FOREIGN KEY (script_id) REFERENCES script(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='镜头表';

CREATE TABLE IF NOT EXISTS image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图片 ID',
    script_id BIGINT NOT NULL COMMENT '关联的剧本 ID',
    image_url VARCHAR(500) NOT NULL COMMENT '图片 URL',
    shot_index INT COMMENT '分镜索引',
    style VARCHAR(50) COMMENT '图片风格',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_image_script FOREIGN KEY (script_id) REFERENCES script(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图片表';

CREATE TABLE IF NOT EXISTS video (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '视频 ID',
    script_id BIGINT NOT NULL COMMENT '关联的剧本 ID',
    video_url VARCHAR(500) NOT NULL COMMENT '视频 URL',
    length INT COMMENT '视频时长（秒）',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '视频状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_video_script FOREIGN KEY (script_id) REFERENCES script(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频表';

CREATE TABLE IF NOT EXISTS generation_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务 ID',
    project_id BIGINT NOT NULL COMMENT '所属项目 ID',
    shot_id BIGINT COMMENT '关联镜头 ID',
    task_type VARCHAR(30) NOT NULL COMMENT '任务类型',
    provider VARCHAR(50) DEFAULT 'mock' COMMENT '提供商',
    model_name VARCHAR(100) COMMENT '模型名称',
    prompt_text TEXT COMMENT '任务提示词',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '任务状态',
    result_url VARCHAR(500) COMMENT '结果地址',
    error_message VARCHAR(500) COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_shot FOREIGN KEY (shot_id) REFERENCES shot(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生成任务表';

CREATE INDEX idx_script_model_config_id ON script(model_config_id);
CREATE INDEX idx_inspiration_source_type ON inspiration(source_type);
CREATE INDEX idx_shot_project_id ON shot(project_id);
CREATE INDEX idx_shot_script_id ON shot(script_id);
CREATE INDEX idx_image_script_id ON image(script_id);
CREATE INDEX idx_video_script_id ON video(script_id);
CREATE INDEX idx_task_project_id ON generation_task(project_id);
CREATE INDEX idx_task_shot_id ON generation_task(shot_id);
CREATE INDEX idx_task_type_status ON generation_task(task_type, status);
CREATE INDEX idx_model_config_enabled ON model_config(is_enabled);
CREATE INDEX idx_model_config_default ON model_config(is_default);
