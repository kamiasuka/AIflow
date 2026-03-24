<template>
  <div class="script-generator">
    <div class="content-wrapper">
      <!-- 左侧：输入区 -->
      <div class="input-section">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>输入</span>
            </div>
          </template>

          <el-form :model="form" label-width="88px">
            <!-- 故事信息输入 -->
            <el-form-item label="故事信息">
              <el-input
                v-model="form.storyInfo"
                type="textarea"
                :rows="5"
                placeholder="请输入故事概要、背景、人物关系等关键信息"
              />
            </el-form-item>

            <!-- 前提条件输入 -->
            <el-form-item label="前提条件">
              <el-input
                v-model="form.premise"
                type="textarea"
                :rows="3"
                placeholder="请输入风格、题材、节奏、输出要求等补充提示"
              />
            </el-form-item>

            <!-- AI 模型选择 -->
            <el-form-item label="AI 模型">
              <el-select
                v-model="form.selectedConfigId"
                placeholder="请选择 AI 模型"
                style="width: 100%"
              >
                <el-option
                  v-for="config in enabledConfigs"
                  :key="config.id"
                  :label="config.modelName"
                  :value="config.id"
                />
              </el-select>
            </el-form-item>

            <!-- 操作按钮 -->
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleGenerate">
                生成剧本
              </el-button>
              <el-button :disabled="!scriptGenerated" @click="exportScript">
                导出剧本
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>

      <!-- 右侧：输出区 -->
      <div class="output-section">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>输出</span>
            </div>
          </template>

          <div class="output-content">
            <div class="output-item">
              <div class="output-label">故事剧本</div>
              <el-input
                v-model="result.storyScript"
                type="textarea"
                :rows="6"
                readonly
                placeholder="生成后的故事剧本会显示在这里"
              />
            </div>

            <div class="output-item">
              <div class="output-label">人物设计</div>
              <el-input
                v-model="result.characterDesign"
                type="textarea"
                :rows="4"
                readonly
                placeholder="人物设计会显示在这里"
              />
            </div>

            <div class="output-item">
              <div class="output-label">分镜脚本</div>
              <el-input
                v-model="result.shotScript"
                type="textarea"
                :rows="4"
                readonly
                placeholder="分镜脚本会显示在这里"
              />
            </div>

            <div class="output-item">
              <div class="output-label">Prompt 提示词</div>
              <el-input
                v-model="result.prompt"
                type="textarea"
                :rows="3"
                readonly
                placeholder="生成后的 prompt 会显示在这里"
              />
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject, onMounted, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchEnabledModelConfigs } from '../api/modelConfig'
import { generateScript } from '../api/script'

const form = ref({
  storyInfo: '',
  premise: '',
  selectedConfigId: null
})

const enabledConfigs = ref([])
const modelConfigKey = inject('modelConfigKey', ref(0))
const scriptGenerated = ref(false)
const loading = ref(false)

const result = ref({
  storyScript: '',
  characterDesign: '',
  shotScript: '',
  prompt: ''
})

/**
 * 加载启用的模型配置，并默认选中数据库中的默认项。
 */
const loadEnabledConfigs = async () => {
  try {
    enabledConfigs.value = await fetchEnabledModelConfigs()

    const defaultConfig = enabledConfigs.value.find((config) => config.isDefault === 1)
    if (defaultConfig) {
      form.value.selectedConfigId = defaultConfig.id
    } else if (enabledConfigs.value.length > 0) {
      form.value.selectedConfigId = enabledConfigs.value[0].id
    } else {
      form.value.selectedConfigId = null
    }
  } catch (error) {
    console.error('Failed to load model configs:', error)
    ElMessage.error(error.message || '加载模型配置失败')
  }
}

/**
 * 生成剧本
 */
const handleGenerate = async () => {
  if (!form.value.storyInfo.trim()) {
    ElMessage.warning('请输入故事信息')
    return
  }
  if (!form.value.premise.trim()) {
    ElMessage.warning('请输入前提条件')
    return
  }
  if (!form.value.selectedConfigId) {
    ElMessage.warning('请先选择 AI 模型')
    return
  }

  loading.value = true
  try {
    const data = await generateScript({
      storyInfo: form.value.storyInfo,
      premise: form.value.premise,
      modelConfigId: form.value.selectedConfigId
    })

    result.value = {
      storyScript: data.storyScript || '',
      characterDesign: data.characterDesign || '',
      shotScript: data.shotScript || '',
      prompt: data.prompt || ''
    }
    scriptGenerated.value = true
    ElMessage.success('剧本生成成功')
  } catch (error) {
    console.error('Failed to generate script:', error)
    ElMessage.error(error.message || '剧本生成失败')
  } finally {
    loading.value = false
  }
}

/**
 * 导出剧本
 */
const exportScript = () => {
  const content = `
=== 故事剧本 ===
${result.value.storyScript}

=== 人物设计 ===
${result.value.characterDesign}

=== 分镜脚本 ===
${result.value.shotScript}

=== Prompt 提示词 ===
${result.value.prompt}
  `.trim()

  const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const anchor = document.createElement('a')
  anchor.href = url
  anchor.download = 'script.txt'
  anchor.click()
  URL.revokeObjectURL(url)
}

watch(
  modelConfigKey,
  () => {
    loadEnabledConfigs()
  },
  { immediate: true }
)

onMounted(() => {
  loadEnabledConfigs()
})
</script>

<style scoped>
.script-generator {
  height: 100%;
}

.content-wrapper {
  display: flex;
  gap: 20px;
  height: 100%;
}

.input-section {
  flex: 1;
}

.output-section {
  flex: 2;
}

.output-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.output-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.output-label {
  font-weight: bold;
  color: #409eff;
  font-size: 14px;
}
</style>