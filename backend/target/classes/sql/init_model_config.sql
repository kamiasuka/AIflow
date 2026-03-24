INSERT INTO model_config (
    model_name,
    api_url,
    api_key,
    model_version,
    max_tokens,
    temperature,
    is_enabled,
    is_default,
    description
) VALUES (
    '豆包 (Doubao)',
    'https://ark.cn-beijing.volces.com/api/v3/chat/completions',
    'your_doubao_api_key_here',
    'ep-20240310155935-fh465',
    4096,
    0.70,
    1,
    1,
    '字节跳动豆包模型示例配置'
);

INSERT INTO model_config (
    model_name,
    api_url,
    api_key,
    model_version,
    max_tokens,
    temperature,
    is_enabled,
    is_default,
    description
) VALUES (
    'DeepSeek',
    'https://api.deepseek.com/v1/chat/completions',
    'your_deepseek_api_key_here',
    'deepseek-chat',
    4096,
    0.70,
    1,
    0,
    'DeepSeek 模型示例配置'
);
