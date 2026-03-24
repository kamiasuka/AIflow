<template>
  <div class="text2image-generator">
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>文生图</span>
        </div>
      </template>

      <el-form :model="form" label-width="88px">
        <el-form-item label="故事脚本">
          <el-input
            v-model="form.storyScript"
            type="textarea"
            :rows="5"
            placeholder="请输入用于生成分镜图的故事脚本"
          />
        </el-form-item>

        <el-form-item label="分镜数量">
          <el-slider v-model="form.shotCount" :min="2" :max="10" :step="1" />
          <span class="shot-count">{{ form.shotCount }} 张</span>
        </el-form-item>

        <el-form-item label="画面风格">
          <el-select v-model="form.style" placeholder="请选择画面风格">
            <el-option label="写实" value="realistic" />
            <el-option label="卡通" value="cartoon" />
            <el-option label="科幻" value="sci-fi" />
            <el-option label="古风" value="ancient" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleGenerateImages">
            生成图片
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div v-if="imagesGenerated" class="result-container">
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>生成的分镜图片</span>
          </div>
        </template>

        <div class="image-grid">
          <div v-for="(image, index) in generatedImages" :key="index" class="image-item">
            <el-image :src="image" fit="cover" />
            <div class="image-caption">第 {{ index + 1 }} 张</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { generateImages } from '../api/image'

const form = ref({
  storyScript: '',
  shotCount: 5,
  style: 'realistic'
})

const imagesGenerated = ref(false)
const loading = ref(false)
const generatedImages = ref([])

const handleGenerateImages = async () => {
  if (!form.value.storyScript.trim()) {
    ElMessage.warning('请输入故事脚本')
    return
  }

  loading.value = true
  try {
    const data = await generateImages({
      storyScript: form.value.storyScript,
      shotCount: form.value.shotCount,
      style: form.value.style
    })

    generatedImages.value = data || []
    imagesGenerated.value = true
    ElMessage.success('图片生成成功')
  } catch (error) {
    console.error('Failed to generate images:', error)
    ElMessage.error(error.message || '图片生成失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.text2image-generator {
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

.shot-count {
  margin-left: 15px;
  font-weight: bold;
}

.result-container {
  margin-top: 20px;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  padding: 20px;
}

.image-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.image-caption {
  padding: 10px;
  text-align: center;
  background-color: #f5f5f5;
  font-size: 14px;
}
</style>