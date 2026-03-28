<template>
  <div class="material-archive">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="card">
          <template #header>
            <div class="card-header">项目素材库</div>
          </template>
          <el-input v-model="keyword" clearable placeholder="搜索项目名称或题材标签" class="search-box" />
          <div class="project-list">
            <div
              v-for="project in filteredProjects"
              :key="project.id"
              :class="['project-item', { active: project.id === activeProjectId }]"
              @click="handleSelectProject(project.id)"
            >
              <div class="project-title">{{ project.name }}</div>
              <div class="project-meta">
                <span>镜头 {{ project.shotCount }}</span>
                <span>图 {{ project.imageTaskCount }}</span>
                <span>视频 {{ project.videoTaskCount }}</span>
              </div>
              <div class="project-tag">{{ project.genreTags || '未设置标签' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>素材详情</span>
              <span v-if="detail.project" class="detail-title">{{ detail.project.name }}</span>
            </div>
          </template>
          <div v-if="!detail.project" class="empty-state">选择左侧项目后查看镜头素材和任务记录</div>
          <template v-else>
            <div class="detail-summary">
              <div>来源：{{ detail.project.sourceTitle || '手动输入' }}</div>
              <div>标签：{{ detail.project.genreTags || '未设置' }}</div>
              <div>状态：{{ detail.project.status || 'draft' }}</div>
            </div>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="镜头素材" name="shots">
                <div class="shot-grid">
                  <div v-for="shot in detail.shotList" :key="shot.shotId" class="shot-card">
                    <el-image v-if="shot.imageUrl" :src="shot.imageUrl" fit="cover" class="shot-image" />
                    <div class="shot-body">
                      <div class="shot-title">镜头 {{ shot.shotNo }}</div>
                      <div class="shot-text">{{ shot.actionDesc }}</div>
                      <div class="shot-prompt">{{ shot.promptText }}</div>
                      <div class="shot-status">
                        <span>图片：{{ shot.imageStatus }}</span>
                        <span>视频：{{ shot.videoStatus }}</span>
                      </div>
                      <a v-if="shot.videoUrl" :href="shot.videoUrl" target="_blank" rel="noreferrer" class="video-link">查看视频结果</a>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="任务记录" name="tasks">
                <el-table :data="detail.taskList" border style="width: 100%">
                  <el-table-column prop="id" label="任务 ID" width="90" />
                  <el-table-column prop="taskType" label="类型" width="100" />
                  <el-table-column prop="provider" label="提供商" width="120" />
                  <el-table-column prop="modelName" label="模型" width="140" />
                  <el-table-column prop="status" label="状态" width="100" />
                  <el-table-column prop="resultUrl" label="结果地址" min-width="220" show-overflow-tooltip />
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </template>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchArchiveProjectDetail, fetchArchiveProjectList } from '../api/archive'

const keyword = ref('')
const activeTab = ref('shots')
const activeProjectId = ref(null)
const projectList = ref([])
const detail = ref({ project: null, shotList: [], taskList: [] })

const filteredProjects = computed(() => {
  const text = keyword.value.trim().toLowerCase()
  if (!text) return projectList.value
  return projectList.value.filter((item) =>
    [item.name, item.genreTags, item.sourceTitle].some((value) =>
      String(value || '').toLowerCase().includes(text)
    )
  )
})

const loadProjectList = async () => {
  try {
    projectList.value = await fetchArchiveProjectList()
    if (projectList.value.length > 0) {
      await handleSelectProject(projectList.value[0].id)
    }
  } catch (error) {
    console.error('Failed to load archive project list:', error)
    ElMessage.error(error.message || '加载项目素材库失败')
  }
}

const handleSelectProject = async (projectId) => {
  activeProjectId.value = projectId
  try {
    detail.value = await fetchArchiveProjectDetail(projectId)
  } catch (error) {
    console.error('Failed to load archive detail:', error)
    ElMessage.error(error.message || '加载素材详情失败')
  }
}

onMounted(() => {
  loadProjectList()
})
</script>

<style scoped>
.material-archive { display: flex; flex-direction: column; gap: 20px; }
.card { min-height: 640px; }
.card-header { display: flex; justify-content: space-between; align-items: center; font-weight: 700; }
.detail-title { color: #409eff; }
.search-box { margin-bottom: 16px; }
.project-list { display: flex; flex-direction: column; gap: 12px; }
.project-item { padding: 12px; border: 1px solid #e4e7ed; border-radius: 10px; cursor: pointer; background: #fff; }
.project-item.active { border-color: #409eff; box-shadow: 0 0 0 1px rgba(64, 158, 255, 0.15); }
.project-title { font-weight: 600; color: #303133; }
.project-meta { display: flex; gap: 12px; margin-top: 8px; color: #606266; font-size: 13px; }
.project-tag { margin-top: 8px; color: #909399; font-size: 13px; }
.empty-state { color: #909399; padding: 24px 0; }
.detail-summary { display: flex; gap: 24px; margin-bottom: 16px; color: #606266; font-size: 14px; }
.shot-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 16px; }
.shot-card { border: 1px solid #e4e7ed; border-radius: 10px; overflow: hidden; background: #fff; }
.shot-image { width: 100%; height: 220px; display: block; }
.shot-body { padding: 12px; display: flex; flex-direction: column; gap: 8px; }
.shot-title { font-weight: 600; color: #303133; }
.shot-text, .shot-prompt, .shot-status { color: #606266; font-size: 13px; line-height: 1.5; }
.shot-status { display: flex; justify-content: space-between; }
.video-link { color: #409eff; text-decoration: none; font-size: 13px; }
</style>
