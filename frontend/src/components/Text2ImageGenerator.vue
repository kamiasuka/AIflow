<template>
  <div class="text2image-generator">
    <!-- 文生图表单 -->
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>文生图</span>
        </div>
      </template>
      <el-form :model="form" label-width="80px">
        <!-- 故事脚本输入 -->
        <el-form-item label="故事脚本">
          <el-input
            v-model="form.storyScript"
            type="textarea"
            rows="4"
            placeholder="请输入故事脚本"
          />
        </el-form-item>
        <!-- 分镜数量设置 -->
        <el-form-item label="分镜数量">
          <el-slider v-model="form.shotCount" :min="2" :max="10" :step="1" />
          <span class="shot-count">{{ form.shotCount }} 帧</span>
        </el-form-item>
        <!-- 风格选择 -->
        <el-form-item label="风格选择">
          <el-select v-model="form.style" placeholder="请选择风格">
            <el-option label="写实" value="realistic" />
            <el-option label="卡通" value="cartoon" />
            <el-option label="科幻" value="sci-fi" />
            <el-option label="古风" value="ancient" />
          </el-select>
        </el-form-item>
        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" @click="generateImages" :loading="loading">生成图片</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 生成结果展示 -->
    <div class="result-container" v-if="imagesGenerated">
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>生成的分镜图片</span>
          </div>
        </template>
        <div class="image-grid">
          <div class="image-item" v-for="(image, index) in generatedImages" :key="index">
            <el-image :src="image" fit="cover" />
            <div class="image-caption">第 {{ index + 1 }} 帧</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

// 表单数据
const form = ref({
  storyScript: '', // 故事脚本
  shotCount: 5, // 分镜数量，默认5帧
  style: 'realistic' // 图片风格，默认写实
})

// 生成状态
const imagesGenerated = ref(false) // 标记图片是否已生成
const loading = ref(false) // 加载状态

// 生成的图片URL列表
const generatedImages = ref([])

/**
 * 生成图片
 * 调用后端API根据故事脚本生成分镜图片
 */
const generateImages = async () => {
  if (!form.value.storyScript.trim()) {
    ElMessage.warning('请输入故事脚本')
    return
  }

  loading.value = true
  try {
    const response = await fetch('/api/image/generate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        storyScript: form.value.storyScript,
        shotCount: form.value.shotCount,
        style: form.value.style
      })
    })

    const res = await response.json()

    if (res.code === 2001) {
      generatedImages.value = res.data || []
      imagesGenerated.value = true
      ElMessage.success('图片生成成功')
    } else {
      ElMessage.error(res.msg || '生成图片失败')
    }
  } catch (error) {
    console.error('生成图片失败:', error)
    ElMessage.error('生成图片失败: ' + error.message)
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
