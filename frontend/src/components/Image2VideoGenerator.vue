<template>
  <div class="image2video-generator">
    <!-- 图生视频表单 -->
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>图生视频</span>
        </div>
      </template>
      <el-form :model="form" label-width="80px">
        <!-- 剧本信息输入 -->
        <el-form-item label="剧本信息">
          <el-input
            v-model="form.script"
            type="textarea"
            rows="3"
            placeholder="请输入剧本信息"
          />
        </el-form-item>
        <!-- 分镜图片上传 -->
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
              <div class="el-upload__text">点击或拖拽文件到此处上传</div>
            </template>
          </el-upload>
        </el-form-item>
        <!-- 视频长度设置 -->
        <el-form-item label="视频长度">
          <el-slider v-model="form.videoLength" :min="1" :max="30" :step="1" />
          <span class="video-length">{{ form.videoLength }} 秒</span>
        </el-form-item>
        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" @click="generateVideo">生成视频</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 生成结果展示 -->
    <div class="result-container" v-if="videoGenerated">
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>生成的视频</span>
          </div>
        </template>
        <div class="video-player">
          <video controls width="100%" height="auto">
            <source src="https://example.com/sample-video.mp4" type="video/mp4">
            您的浏览器不支持视频播放。
          </video>
        </div>
        <el-button type="primary" style="margin-top: 20px">下载视频</el-button>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'

// 表单数据
const form = ref({
  script: '', // 剧本信息
  videoLength: 10 // 视频长度，默认10秒
})

// 上传的文件列表
const fileList = ref([])

// 生成状态
const videoGenerated = ref(false) // 标记视频是否已生成

/**
 * 处理图片上传
 * @param {Object} file 上传的文件
 * @param {Array} newFileList 新的文件列表
 */
const handleImageChange = (file, newFileList) => {
  fileList.value = newFileList
}

/**
 * 生成视频
 * 调用后端API将分镜图片和剧本转换为视频
 */
const generateVideo = async () => {
  try {
    const response = await fetch('/api/video/generate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        script: form.script,
        videoLength: form.videoLength
      })
    })
    const data = await response.json()
    videoGenerated.value = true
  } catch (error) {
    console.error('生成视频失败:', error)
  }
}
</script>

<style scoped>
.image2video-generator {
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

.video-length {
  margin-left: 10px;
  font-size: 14px;
  color: #606266;
}

.result-container {
  margin-top: 20px;
}

.video-player {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}
</style>