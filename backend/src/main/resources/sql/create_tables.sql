-- 创建剧本表
CREATE TABLE IF NOT EXISTS script (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '剧本ID',
    story_info TEXT COMMENT '故事信息',
    story_script TEXT COMMENT '故事剧本',
    character_design TEXT COMMENT '人物设计',
    shot_script TEXT COMMENT '分镜脚本',
    prompt TEXT COMMENT 'Prompt提示词',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='剧本表';

-- 创建图片表
CREATE TABLE IF NOT EXISTS image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图片ID',
    script_id BIGINT NOT NULL COMMENT '关联的剧本ID',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    shot_index INT COMMENT '分镜索引',
    style VARCHAR(50) COMMENT '图片风格',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (script_id) REFERENCES script(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图片表';

-- 创建视频表
CREATE TABLE IF NOT EXISTS video (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '视频ID',
    script_id BIGINT NOT NULL COMMENT '关联的剧本ID',
    video_url VARCHAR(500) NOT NULL COMMENT '视频URL',
    length INT COMMENT '视频长度（秒）',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '视频状态（success/failed/pending）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (script_id) REFERENCES script(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频表';

-- 创建大模型配置表
CREATE TABLE IF NOT EXISTS model_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    model_name VARCHAR(100) NOT NULL COMMENT '模型名称',
    model_type VARCHAR(50) NOT NULL COMMENT '模型类型（如 doubao/deepseek）',
    api_url VARCHAR(500) NOT NULL COMMENT 'API地址',
    api_key VARCHAR(255) NOT NULL COMMENT 'API密钥',
    model_version VARCHAR(100) COMMENT '模型版本',
    max_tokens INT DEFAULT 4096 COMMENT '最大token数',
    temperature DECIMAL(3,2) DEFAULT 0.7 COMMENT '温度参数',
    is_enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用（0-禁用，1-启用）',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认（0-否，1-是）',
    description VARCHAR(500) COMMENT '模型描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='大模型配置表';

-- 创建索引
CREATE INDEX idx_image_script_id ON image(script_id);
CREATE INDEX idx_video_script_id ON video(script_id);
CREATE INDEX idx_model_config_type ON model_config(model_type);
CREATE INDEX idx_model_config_enabled ON model_config(is_enabled);
