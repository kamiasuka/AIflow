<template>
  <div class="app-container">
    <el-header height="60px" class="header">
      <h1>AIflow - AI Workflow Studio</h1>
    </el-header>

    <el-main class="main">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Script" name="script">
          <script-generator-panel />
        </el-tab-pane>
        <el-tab-pane label="Text to Image" name="text2image">
          <text2-image-panel />
        </el-tab-pane>
        <el-tab-pane label="Image to Video" name="image2video">
          <image2-video-panel />
        </el-tab-pane>
        <el-tab-pane label="Materials" name="material">
          <material-organizer-panel />
        </el-tab-pane>
      </el-tabs>
    </el-main>

    <el-footer height="40px" class="footer">
      <p>(c) 2026 AIflow. All rights reserved.</p>
    </el-footer>

    <model-config-dialog-panel @config-updated="handleConfigUpdated" />
  </div>
</template>

<script setup>
import { provide, ref } from 'vue'
import Image2VideoPanel from './components/Image2VideoPanel.vue'
import MaterialOrganizerPanel from './components/MaterialOrganizerPanel.vue'
import ModelConfigDialogPanel from './components/ModelConfigDialogPanel.vue'
import ScriptGeneratorPanel from './components/ScriptGeneratorPanel.vue'
import Text2ImagePanel from './components/Text2ImagePanel.vue'

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
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  padding-left: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header h1 {
  margin: 0;
  font-size: 20px;
}

.main {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}

.footer {
  background-color: #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #909399;
}
</style>
