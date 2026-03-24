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

CREATE INDEX idx_script_model_config_id ON script(model_config_id);
CREATE INDEX idx_image_script_id ON image(script_id);
CREATE INDEX idx_video_script_id ON video(script_id);
CREATE INDEX idx_model_config_enabled ON model_config(is_enabled);
CREATE INDEX idx_model_config_default ON model_config(is_default);
