<template>
  <div class="material-organizer">
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>素材整理</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="批量重命名" name="rename">
          <el-form :model="renameForm" label-width="100px">
            <el-form-item label="文件类型">
              <el-select v-model="renameForm.fileType" placeholder="请选择文件类型">
                <el-option label="视频文件" value="video" />
                <el-option label="音频文件" value="audio" />
                <el-option label="图片文件" value="image" />
                <el-option label="全部文件" value="all" />
              </el-select>
            </el-form-item>
            <el-form-item label="命名模式">
              <el-input v-model="renameForm.pattern" placeholder="例如：scene_{index}" />
            </el-form-item>
            <el-form-item label="起始编号">
              <el-input-number v-model="renameForm.startIndex" :min="1" :step="1" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="batchRename">批量重命名</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="分类整理" name="categorize">
          <el-form :model="categorizeForm" label-width="100px">
            <el-form-item label="分类方式">
              <el-select v-model="categorizeForm.categorizeBy" placeholder="请选择分类方式">
                <el-option label="文件类型" value="type" />
                <el-option label="创建日期" value="date" />
                <el-option label="自定义标签" value="tag" />
              </el-select>
            </el-form-item>
            <el-form-item label="目标目录">
              <el-input v-model="categorizeForm.targetDir" placeholder="请输入目标目录路径" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="categorizeFiles">分类整理</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <div v-if="operationResult" class="result-container">
      <el-card class="card">
        <template #header>
          <div class="card-header">
            <span>操作结果</span>
          </div>
        </template>
        <el-alert
          :title="operationResult.title"
          :message="operationResult.message"
          :type="operationResult.type"
          show-icon
        />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref('rename')

const renameForm = ref({
  fileType: 'all',
  pattern: 'file_{index}',
  startIndex: 1
})

const categorizeForm = ref({
  categorizeBy: 'type',
  targetDir: ''
})

const operationResult = ref(null)

const batchRename = async () => {
  // 模拟批量重命名操作
  await new Promise((resolve) => setTimeout(resolve, 1000))
  operationResult.value = {
    title: '批量重命名成功',
    message: `已按模式 "${renameForm.value.pattern}" 重命名素材文件。`,
    type: 'success'
  }
}

const categorizeFiles = async () => {
  // 模拟分类整理操作
  await new Promise((resolve) => setTimeout(resolve, 1000))
  operationResult.value = {
    title: '分类整理成功',
    message: `已按 ${getCategorizeLabel(categorizeForm.value.categorizeBy)} 完成文件分类。`,
    type: 'success'
  }
}

const getCategorizeLabel = (value) => {
  const labels = {
    type: '文件类型',
    date: '创建日期',
    tag: '自定义标签'
  }
  return labels[value] || value
}
</script>

<style scoped>
.material-organizer {
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
  margin-top: 20px;
}
</style>