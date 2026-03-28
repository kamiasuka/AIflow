<template>
  <div class="app-container">
    <el-header height="60px" class="header">
      <h1>AIflow 智能创作工作台</h1>
    </el-header>

    <el-main class="main">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="脚本生成" name="script">
          <script-generator-panel />
        </el-tab-pane>
        <el-tab-pane label="文生图" name="text2image">
          <text2-image-workflow-panel />
        </el-tab-pane>
        <el-tab-pane label="图生视频" name="image2video">
          <image2-video-workflow-panel />
        </el-tab-pane>
        <el-tab-pane label="素材工作台" name="material">
          <material-studio-panel />
        </el-tab-pane>
      </el-tabs>
    </el-main>

    <el-footer height="40px" class="footer">
      <p>AIflow 2026 智能内容生产台</p>
    </el-footer>

    <model-config-dialog-panel @config-updated="handleConfigUpdated" />
  </div>
</template>

<script setup>
import { provide, ref } from 'vue'
import Image2VideoWorkflowPanel from './components/Image2VideoWorkflowPanel.vue'
import MaterialStudioPanel from './components/MaterialStudioPanel.vue'
import ModelConfigDialogPanel from './components/ModelConfigDialogPanel.vue'
import ScriptGeneratorPanel from './components/ScriptGeneratorPanel.vue'
import Text2ImageWorkflowPanel from './components/Text2ImageWorkflowPanel.vue'

const activeTab = ref('script')
const modelConfigKey = ref(0)

const handleConfigUpdated = () => {
  modelConfigKey.value += 1
}

provide('modelConfigKey', modelConfigKey)
</script>

<style scoped>
.app-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: linear-gradient(90deg, #1d4ed8, #2563eb);
  color: #fff;
  display: flex;
  align-items: center;
  padding-left: 20px;
  box-shadow: 0 2px 8px rgba(37, 99, 235, 0.18);
}

.header h1 {
  margin: 0;
  font-size: 20px;
  letter-spacing: 0.5px;
}

.main {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f5f7fa;
}

.footer {
  background: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #6b7280;
}
</style>
