-- 大模型配置初始化数据

-- 插入豆包配置
INSERT INTO model_config (model_name, model_type, api_url, api_key, model_version,
                         max_tokens, temperature, is_enabled, is_default, description)
VALUES ('豆包 (Doubao)', 'doubao', 'https://ark.cn-beijing.volces.com/api/v3/chat/completions',
        'your_doubao_api_key_here', 'ep-20240310155935-fh465',
        4096, 0.7, 1, 1, '字节跳动豆包大模型 API');

-- 插入DeepSeek配置
INSERT INTO model_config (model_name, model_type, api_url, api_key, model_version,
                         max_tokens, temperature, is_enabled, is_default, description)
VALUES ('DeepSeek', 'deepseek', 'https://api.deepseek.com/v1/chat/completions',
        'your_deepseek_api_key_here', 'deepseek-chat',
        4096, 0.7, 1, 0, 'DeepSeek大模型 API');
