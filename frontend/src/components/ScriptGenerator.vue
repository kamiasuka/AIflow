<template>
  <div class="script-generator">
    <div class="content-wrapper">
      <!-- 左侧：输入区域 -->
      <div class="input-section">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>输入</span>
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
            <el-form-item label="AI模型">
              <el-select v-model="form.selectedConfigId" placeholder="请选择AI模型" style="width: 100%">
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
              <el-button type="primary" @click="generateScript" :loading="loading">生成剧本</el-button>
              <el-button @click="exportScript" :disabled="!scriptGenerated">导出剧本</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>

      <!-- 右侧：输出区域 -->
      <div class="output-section">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>输出</span>
            </div>
          </template>
          <div class="output-content">
            <!-- 故事剧本 -->
            <div class="output-item">
              <div class="output-label">故事剧本</div>
              <el-input
                v-model="result.storyScript"
                type="textarea"
                rows="4"
                readonly
                placeholder="生成的剧本将显示在这里"
              />
            </div>

            <!-- 人物设计 -->
            <div class="output-item">
              <div class="output-label">人物设计</div>
              <el-input
                v-model="result.characterDesign"
                type="textarea"
                rows="3"
                readonly
                placeholder="人物设计将显示在这里"
              />
            </div>

            <!-- 分镜脚本 -->
            <div class="output-item">
              <div class="output-label">分镜脚本</div>
              <el-input
                v-model="result.shotScript"
                type="textarea"
                rows="3"
                readonly
                placeholder="分镜脚本将显示在这里"
              />
            </div>

            <!-- Prompt提示词 -->
            <div class="output-item">
              <div class="output-label">Prompt提示词</div>
              <el-input
                v-model="result.prompt"
                type="textarea"
                rows="2"
                readonly
                placeholder="生成的Prompt将显示在这里"
              />
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, inject } from 'vue'
import { ElMessage } from 'element-plus'

// 表单数据
const form = ref({
  storyInfo: '',
  premise: '',
  selectedConfigId: null
})

// 启用的模型配置
const enabledConfigs = ref([])

// 注入来自App.vue的modelConfigKey，用于监听模型配置更新
const modelConfigKey = inject('modelConfigKey', ref(0))

// 生成状态
const scriptGenerated = ref(false)
const loading = ref(false)

// 生成结果
const result = ref({
  storyScript: '',
  characterDesign: '',
  shotScript: '',
  prompt: ''
})

/**
 * 加载启用的模型配置
 */
const loadEnabledConfigs = async () => {
  try {
    const response = await fetch('http://localhost:8081/api/model-config/enabled')
    const res = await response.json()
    if (res.code === 2001) {
      enabledConfigs.value = res.data || []
      const defaultConfig = enabledConfigs.value.find(config => config.isDefault === 1)
      if (defaultConfig) {
        form.value.selectedConfigId = defaultConfig.id
      } else if (enabledConfigs.value.length > 0) {
        form.value.selectedConfigId = enabledConfigs.value[0].id
      }
    }
  } catch (error) {
    console.error('加载模型配置失败:', error)
  }
}

/**
 * 生成剧本
 */
const generateScript = async () => {
  if (!form.value.storyInfo.trim()) {
    ElMessage.warning('请输入故事信息')
    return
  }
  if (!form.value.premise.trim()) {
    ElMessage.warning('请输入前提条件')
    return
  }

  loading.value = true
  try {
    const selectedConfig = enabledConfigs.value.find(c => c.id === form.value.selectedConfigId)
    if (!selectedConfig) {
      ElMessage.warning('请先选择AI模型')
      loading.value = false
      return
    }

    const response = await fetch('http://localhost:8081/api/script/generate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        storyInfo: form.value.storyInfo,
        premise: form.value.premise,
        apiType: selectedConfig.modelName
      })
    })

    const res = await response.json()

    if (res.code === 2001) {
      const data = res.data
      result.value = {
        storyScript: data.storyScript || '',
        characterDesign: data.characterDesign || '',
        shotScript: data.shotScript || '',
        prompt: data.prompt || ''
      }
      scriptGenerated.value = true
      ElMessage.success('剧本生成成功')
    } else {
      ElMessage.error(res.msg || '生成剧本失败')
    }
  } catch (error) {
    console.error('生成剧本失败:', error)
    ElMessage.error('生成剧本失败: ' + error.message)
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

=== Prompt提示词 ===
${result.value.prompt}
  `.trim()

  const blob = new Blob([content], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'script.txt'
  a.click()
  URL.revokeObjectURL(url)
}

// 监听modelConfigKey变化，当模型配置更新时自动刷新
watch(modelConfigKey, () => {
  loadEnabledConfigs()
}, { immediate: true })

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
