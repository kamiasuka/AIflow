<template>
  <div class="image2video-panel">
    <el-card class="card">
      <template #header>
        <div class="card-header">图生视频</div>
      </template>

      <el-form :model="form" label-width="96px">
        <el-form-item label="项目 ID">
          <el-input v-model="form.projectId" placeholder="输入文生图返回的项目 ID" />
        </el-form-item>
        <el-form-item label="镜头 ID">
          <el-input v-model="form.shotId" placeholder="输入需要生成视频的镜头 ID" />
        </el-form-item>
        <el-form-item label="脚本信息">
          <el-input v-model="form.script" type="textarea" :rows="4" placeholder="输入该镜头的剧情描述或旁白信息" />
        </el-form-item>
        <el-form-item label="参考图片">
          <el-input v-model="form.imageUrl" placeholder="输入该镜头的图片地址，用于记录和后续接真实模型" />
        </el-form-item>
        <el-form-item label="运动提示词">
          <el-input v-model="form.motionPrompt" type="textarea" :rows="3" placeholder="例如：人物缓慢转身，衣摆随风摆动，镜头轻微推进" />
        </el-form-item>
        <el-form-item label="镜头运动">
          <el-select v-model="form.cameraMotion" placeholder="请选择镜头运动">
            <el-option label="固定镜头" value="固定镜头" />
            <el-option label="缓慢推进" value="缓慢推进" />
            <el-option label="横向平移" value="横向平移" />
            <el-option label="轻微环绕" value="轻微环绕" />
          </el-select>
        </el-form-item>
        <el-form-item label="视频时长">
          <el-slider v-model="form.videoLength" :min="1" :max="15" :step="1" />
          <span class="video-length">{{ form.videoLength }} 秒</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleGenerateVideo">生成视频任务</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div v-if="result.videoUrl" class="result-container">
      <el-card class="card">
        <template #header>
          <div class="card-header">生成结果</div>
        </template>
        <div class="result-meta">
          <div>任务 ID：{{ result.taskId }}</div>
          <div>任务状态：{{ result.taskStatus }}</div>
        </div>
        <div class="video-player">
          <video controls width="100%" height="auto">
            <source :src="result.videoUrl" type="video/mp4" />
            当前浏览器不支持视频播放。
          </video>
        </div>
        <el-button type="primary" style="margin-top: 20px" @click="downloadVideo">下载视频</el-button>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { generateVideoWorkflow } from '../api/video'

const form = ref({
  projectId: '',
  shotId: '',
  script: '',
  imageUrl: '',
  motionPrompt: '',
  cameraMotion: '缓慢推进',
  videoLength: 6
})

const loading = ref(false)
const result = ref({
  taskId: null,
  taskStatus: '',
  videoUrl: ''
})

const handleGenerateVideo = async () => {
  if (!form.value.projectId.trim()) {
    ElMessage.warning('请输入项目 ID')
    return
  }
  if (!form.value.shotId.trim()) {
    ElMessage.warning('请输入镜头 ID')
    return
  }
  if (!form.value.script.trim()) {
    ElMessage.warning('请输入脚本信息')
    return
  }

  loading.value = true
  try {
    const data = await generateVideoWorkflow({
      projectId: Number(form.value.projectId),
      shotId: Number(form.value.shotId),
      script: form.value.script,
      imageUrl: form.value.imageUrl,
      motionPrompt: form.value.motionPrompt,
      cameraMotion: form.value.cameraMotion,
      videoLength: form.value.videoLength
    })

    result.value = data || { taskId: null, taskStatus: '', videoUrl: '' }
    ElMessage.success('图生视频任务已生成')
  } catch (error) {
    console.error('Failed to generate video workflow:', error)
    ElMessage.error(error.message || '图生视频任务创建失败')
  } finally {
    loading.value = false
  }
}

const downloadVideo = () => {
  if (!result.value.videoUrl) {
    return
  }

  const anchor = document.createElement('a')
  anchor.href = result.value.videoUrl
  anchor.download = 'storyboard_video.mp4'
  anchor.click()
}
</script>

<style scoped>
.image2video-panel { display: flex; flex-direction: column; gap: 20px; }
.card { width: 100%; }
.card-header { font-weight: bold; font-size: 16px; }
.video-length { margin-left: 15px; font-weight: bold; }
.result-container { margin-top: 4px; }
.result-meta { display: flex; gap: 24px; color: #606266; font-size: 14px; }
.video-player { margin-top: 16px; }
</style>
