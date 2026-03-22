<template>
  <div class="app-container">
    <el-header height="60px" class="header">
      <h1>AIflow - AI自动化工作流</h1>
    </el-header>
    <el-main class="main">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="生成剧本" name="script">
          <script-generator />
        </el-tab-pane>
        <el-tab-pane label="文生图" name="text2image">
          <text2image-generator />
        </el-tab-pane>
        <el-tab-pane label="图生视频" name="image2video">
          <image2video-generator />
        </el-tab-pane>
        <el-tab-pane label="素材整理" name="material">
          <material-organizer />
        </el-tab-pane>
      </el-tabs>
    </el-main>
    <el-footer height="40px" class="footer">
      <p>© 2026 AIflow. All rights reserved.</p>
    </el-footer>

    <!-- 大模型配置弹窗 -->
    <model-config-dialog @config-updated="handleConfigUpdated" />
  </div>
</template>

<script setup>
import { ref, provide } from 'vue'
import ScriptGenerator from './components/ScriptGenerator.vue'
import Text2ImageGenerator from './components/Text2ImageGenerator.vue'
import Image2VideoGenerator from './components/Image2VideoGenerator.vue'
import MaterialOrganizer from './components/MaterialOrganizer.vue'
import ModelConfigDialog from './components/ModelConfigDialog.vue'

const activeTab = ref('script')

// 用于触发模型配置刷新的key
const modelConfigKey = ref(0)

// 当模型配置更新时，递增key来触发ScriptGenerator刷新
const handleConfigUpdated = () => {
  modelConfigKey.value++
}

// 提供给子组件使用
provide('modelConfigKey', modelConfigKey)
</script>

<style scoped>
.app-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #409EFF;
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
