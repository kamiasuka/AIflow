<template>
  <div class="image2video-generator">
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>图生视频</span>
        </div>
      </template>

      <el-form :model="form" label-width="96px">
        <el-form-item label="剧本信息">
          <el-input
            v-model="form.script"
            type="textarea"
            :rows="4"
            placeholder="请输入用于生成视频的剧本或分镜说明"
          />
        </el-form-item>

        <el-form-item label="分镜图片">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleImageChange"
            :file-list="fileList"
            list-type="picture-card"
            multiple
          >
            <template #default>
              <el-icon><Plus /></el-icon>
              <div class="el-upload__text">点击或拖拽文件到这里上传</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="视频时长">
          <el-slider v-model="form.videoLength" :min="1" :max="30" :step="1" />
          <span class="video-length">{{ form.videoLength }} 秒</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleGenerateVideo">
            生成视频
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div v-if="videoGenerated" class="result-container">
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>生成结果</span>
          </div>
        </template>

        <div class="video-player">
          <video controls width="100%" height="auto">
            <source :src="videoUrl" type="video/mp4" />
            当前浏览器不支持视频播放。
          </video>
        </div>

        <el-button type="primary" style="margin-top: 20px" @click="downloadVideo">
          下载视频
        </el-button>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { generateVideo } from '../api/video'

const form = ref({
  script: '',
  videoLength: 10
})

const fileList = ref([])
const videoGenerated = ref(false)
const loading = ref(false)
const videoUrl = ref('')

const handleImageChange = (_file, newFileList) => {
  fileList.value = newFileList
}

const handleGenerateVideo = async () => {
  if (!form.value.script.trim()) {
    ElMessage.warning('请输入剧本信息')
    return
  }

  if (fileList.value.length === 0) {
    ElMessage.warning('请先上传分镜图片')
    return
  }

  loading.value = true
  try {
    const data = await generateVideo({
      script: form.value.script,
      videoLength: form.value.videoLength
    })
    videoUrl.value = data
    videoGenerated.value = true
    ElMessage.success('视频生成成功')
  } catch (error) {
    console.error('Failed to generate video:', error)
    ElMessage.error(error.message || '视频生成失败')
  } finally {
    loading.value = false
  }
}

const downloadVideo = () => {
  if (!videoUrl.value) {
    return
  }

  const anchor = document.createElement('a')
  anchor.href = videoUrl.value
  anchor.download = 'generated_video.mp4'
  anchor.click()
}
</script>

<style scoped>
.image2video-generator {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  width: 100%;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.video-length {
  margin-left: 15px;
  font-weight: bold;
}

.result-container {
  margin-top: 20px;
}

.video-player {
  margin-top: 10px;
}
</style>