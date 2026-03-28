<template>
  <div class="text2image-panel">
    <el-card class="card">
      <template #header>
        <div class="card-header">文生图</div>
      </template>

      <el-form :model="form" label-width="96px">
        <el-form-item label="项目名称">
          <el-input v-model="form.projectName" placeholder="输入本次项目名称，便于后续归档" />
        </el-form-item>
        <el-form-item label="故事脚本">
          <el-input v-model="form.storyScript" type="textarea" :rows="5" placeholder="输入完整故事脚本或大纲，用于生成镜头图" />
        </el-form-item>
        <el-form-item label="分镜脚本">
          <el-input v-model="form.shotScript" type="textarea" :rows="4" placeholder="可选。输入已有分镜脚本，系统会优先按分镜拆解镜头" />
        </el-form-item>
        <el-form-item label="镜头数量">
          <el-slider v-model="form.shotCount" :min="1" :max="10" :step="1" />
          <span class="shot-count">{{ form.shotCount }} 个</span>
        </el-form-item>
        <el-form-item label="画面风格">
          <el-select v-model="form.style" placeholder="请选择画面风格">
            <el-option label="写实电影" value="写实电影" />
            <el-option label="国风奇幻" value="国风奇幻" />
            <el-option label="赛博科幻" value="赛博科幻" />
            <el-option label="轻漫画风" value="轻漫画风" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleGenerateImages">生成分镜图</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div v-if="workflow.project" class="result-container">
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>项目结果</span>
            <span class="project-name">{{ workflow.project.name }}</span>
          </div>
        </template>

        <div class="image-grid">
          <div v-for="item in workflow.items" :key="item.shot.id || item.shot.shotNo" class="image-item">
            <el-image :src="item.imageUrl" fit="cover" class="image-preview" />
            <div class="image-meta">
              <div class="shot-title">镜头 {{ item.shot.shotNo }}</div>
              <div class="shot-text">{{ item.shot.actionDesc }}</div>
              <div class="shot-prompt">{{ item.shot.promptText }}</div>
              <div class="shot-status">任务状态：{{ item.taskStatus }}</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { generateImageWorkflow } from '../api/image'

const form = ref({
  projectName: '',
  storyScript: '',
  shotScript: '',
  shotCount: 5,
  style: '写实电影'
})

const loading = ref(false)
const workflow = ref({
  project: null,
  items: []
})

const handleGenerateImages = async () => {
  if (!form.value.storyScript.trim()) {
    ElMessage.warning('请输入故事脚本')
    return
  }

  loading.value = true
  try {
    const data = await generateImageWorkflow({
      projectName: form.value.projectName,
      storyScript: form.value.storyScript,
      shotScript: form.value.shotScript,
      shotCount: form.value.shotCount,
      style: form.value.style
    })

    workflow.value = data || { project: null, items: [] }
    ElMessage.success('分镜图任务已生成')
  } catch (error) {
    console.error('Failed to generate image workflow:', error)
    ElMessage.error(error.message || '文生图任务创建失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.text2image-panel { display: flex; flex-direction: column; gap: 20px; }
.card { width: 100%; }
.card-header { display: flex; align-items: center; justify-content: space-between; font-weight: bold; font-size: 16px; }
.project-name { color: #409eff; font-size: 14px; }
.shot-count { margin-left: 15px; font-weight: bold; }
.result-container { margin-top: 4px; }
.image-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 20px; }
.image-item { overflow: hidden; border: 1px solid #e4e7ed; border-radius: 10px; background: #fff; }
.image-preview { width: 100%; height: 220px; display: block; }
.image-meta { padding: 12px; display: flex; flex-direction: column; gap: 8px; }
.shot-title { color: #303133; font-weight: 600; }
.shot-text, .shot-prompt, .shot-status { color: #606266; font-size: 13px; line-height: 1.5; }
</style>
