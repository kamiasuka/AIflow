<template>
  <div class="script-generator">
    <!-- 剧本生成表单 -->
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>剧本生成</span>
        </div>
      </template>
      <el-form :model="form" label-width="80px">
        <!-- 故事信息输入 -->
        <el-form-item label="故事信息">
          <el-input
            v-model="form.storyInfo"
            type="textarea"
            rows="4"
            placeholder="请输入故事大概信息"
          />
        </el-form-item>
        <!-- 前提条件输入 -->
        <el-form-item label="前提条件">
          <el-input
            v-model="form.premise"
            type="textarea"
            rows="2"
            placeholder="请输入自定义前提条件提示"
          />
        </el-form-item>
        <!-- API选择 -->
        <el-form-item label="API选择">
          <el-radio-group v-model="form.apiType">
            <el-radio label="doubao">豆包</el-radio>
            <el-radio label="deepseek">DeepSeek</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" @click="generateScript">生成剧本</el-button>
          <el-button @click="exportScript" :disabled="!scriptGenerated">导出剧本</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 生成结果展示 -->
    <div class="result-container" v-if="scriptGenerated">
      <!-- 故事剧本 -->
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>故事剧本</span>
          </div>
        </template>
        <el-input
          v-model="result.storyScript"
          type="textarea"
          rows="6"
          readonly
        />
      </el-card>

      <!-- 人物设计 -->
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>人物设计</span>
          </div>
        </template>
        <el-input
          v-model="result.characterDesign"
          type="textarea"
          rows="4"
          readonly
        />
      </el-card>

      <!-- 分镜脚本 -->
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>分镜脚本</span>
          </div>
        </template>
        <el-input
          v-model="result.shotScript"
          type="textarea"
          rows="4"
          readonly
        />
      </el-card>

      <!-- Prompt提示词 -->
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>Prompt提示词</span>
          </div>
        </template>
        <el-input
          v-model="result.prompt"
          type="textarea"
          rows="3"
          readonly
        />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 表单数据
const form = ref({
  storyInfo: '', // 故事信息
  premise: '', // 前提条件
  apiType: 'doubao' // API类型，默认使用豆包
})

// 生成状态
const scriptGenerated = ref(false) // 标记剧本是否已生成

// 生成结果
const result = ref({
  storyScript: '', // 故事剧本
  characterDesign: '', // 人物设计
  shotScript: '', // 分镜脚本
  prompt: '' // Prompt提示词
})

/**
 * 生成剧本
 * 调用后端API生成完整剧本信息
 */
const generateScript = async () => {
  try {
    const response = await fetch('/api/script/generate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        storyInfo: form.storyInfo,
        premise: form.premise,
        apiType: form.apiType
      })
    })
    const data = await response.json()
    result.value = {
      storyScript: data.storyScript,
      characterDesign: data.characterDesign,
      shotScript: data.shotScript,
      prompt: data.prompt
    }
    scriptGenerated.value = true
  } catch (error) {
    console.error('生成剧本失败:', error)
  }
}

/**
 * 导出剧本
 * 将生成的剧本信息导出为文本文件
 */
const exportScript = () => {
  // 导出逻辑
  const content = `故事剧本:\n${result.value.storyScript}\n\n人物设计:\n${result.value.characterDesign}\n\n分镜脚本:\n${result.value.shotScript}\n\nPrompt提示词:\n${result.value.prompt}`
  const blob = new Blob([content], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'script.txt'
  a.click()
  URL.revokeObjectURL(url)
}
</script>

<style scoped>
.script-generator {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(45%, 1fr));
  gap: 20px;
}

@media (max-width: 768px) {
  .result-container {
    grid-template-columns: 1fr;
  }
}
</style>