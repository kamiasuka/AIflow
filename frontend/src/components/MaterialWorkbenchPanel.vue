<template>
  <div class="material-workbench">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="项目素材库" name="archive">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="card">
              <template #header><div class="card-header">项目素材库</div></template>
              <el-input v-model="keyword" clearable placeholder="搜索项目名称或题材标签" class="search-box" />
              <div class="project-list">
                <div v-for="project in filteredProjects" :key="project.id" :class="['project-item', { active: project.id === activeProjectId }]" @click="handleSelectProject(project.id)">
                  <div class="project-title">{{ project.name }}</div>
                  <div class="project-meta"><span>镜头 {{ project.shotCount }}</span><span>图 {{ project.imageTaskCount }}</span><span>视频 {{ project.videoTaskCount }}</span></div>
                  <div class="project-tag">{{ project.genreTags || '未设置标签' }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="16">
            <el-card class="card">
              <template #header><div class="card-header"><span>素材详情</span><span v-if="detail.project" class="detail-title">{{ detail.project.name }}</span></div></template>
              <div v-if="!detail.project" class="empty-state">选择左侧项目后查看镜头素材和任务记录</div>
              <template v-else>
                <div class="detail-summary"><div>来源：{{ detail.project.sourceTitle || '手动输入' }}</div><div>标签：{{ detail.project.genreTags || '未设置' }}</div><div>状态：{{ detail.project.status || 'draft' }}</div></div>
                <el-tabs v-model="archiveTab">
                  <el-tab-pane label="镜头素材" name="shots">
                    <div class="shot-grid">
                      <div v-for="shot in detail.shotList" :key="shot.shotId" class="shot-card">
                        <el-image v-if="shot.imageUrl" :src="shot.imageUrl" fit="cover" class="shot-image" />
                        <div class="shot-body">
                          <div class="shot-title">镜头 {{ shot.shotNo }}</div>
                          <div class="shot-text">{{ shot.actionDesc }}</div>
                          <div class="shot-prompt">{{ shot.promptText }}</div>
                          <div class="shot-status"><span>图片：{{ shot.imageStatus }}</span><span>视频：{{ shot.videoStatus }}</span></div>
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
      </el-tab-pane>
      <el-tab-pane label="灵感采集" name="inspiration">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-card class="card">
              <template #header><div class="card-header"><span>灵感池</span><el-button type="primary" link @click="handleImportDemo">导入示例热题材</el-button></div></template>
              <el-form :model="inspirationForm" label-width="88px">
                <el-form-item label="标题"><el-input v-model="inspirationForm.title" placeholder="例如：末世小队生存" /></el-form-item>
                <el-form-item label="平台"><el-input v-model="inspirationForm.sourcePlatform" placeholder="例如：番茄小说" /></el-form-item>
                <el-form-item label="分类"><el-input v-model="inspirationForm.category" placeholder="例如：规则怪谈" /></el-form-item>
                <el-form-item label="标签"><el-input v-model="inspirationForm.tags" placeholder="多个标签用逗号分隔" /></el-form-item>
                <el-form-item label="热度"><el-input v-model="inspirationForm.hotValue" placeholder="例如：高热 / 上升" /></el-form-item>
                <el-form-item label="摘要"><el-input v-model="inspirationForm.summary" type="textarea" :rows="4" placeholder="输入简介摘要，后续可一键转项目" /></el-form-item>
                <el-form-item><el-button type="primary" :loading="inspirationLoading" @click="handleCreateInspiration">保存灵感</el-button></el-form-item>
              </el-form>
            </el-card>
          </el-col>
          <el-col :span="14">
            <el-card class="card">
              <template #header><div class="card-header">已收集灵感</div></template>
              <div class="inspiration-list">
                <div v-for="item in inspirationList" :key="item.id" class="inspiration-item">
                  <div class="inspiration-top">
                    <div>
                      <div class="project-title">{{ item.title }}</div>
                      <div class="project-meta"><span>{{ item.sourcePlatform || '手动录入' }}</span><span>{{ item.category || '未分类' }}</span><span>{{ item.hotValue || '普通' }}</span></div>
                    </div>
                    <el-button type="primary" link @click="handleCreateProject(item.id)">转为项目</el-button>
                  </div>
                  <div class="project-tag">{{ item.tags || '未设置标签' }}</div>
                  <div class="shot-text">{{ item.summary }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchArchiveProjectDetail, fetchArchiveProjectList } from '../api/archive'
import { createInspiration, createProjectFromInspiration, fetchInspirationList, importDemoInspirations } from '../api/inspiration'
const activeTab = ref('archive')
const archiveTab = ref('shots')
const keyword = ref('')
const activeProjectId = ref(null)
const projectList = ref([])
const inspirationList = ref([])
const inspirationLoading = ref(false)
const inspirationForm = ref({ title: '', sourcePlatform: '', sourceType: 'manual', category: '', tags: '', summary: '', hotValue: '' })
const detail = ref({ project: null, shotList: [], taskList: [] })
const filteredProjects = computed(() => {
  const text = keyword.value.trim().toLowerCase()
  if (!text) return projectList.value
  return projectList.value.filter((item) => [item.name, item.genreTags, item.sourceTitle].some((value) => String(value || '').toLowerCase().includes(text)))
})
const loadProjectList = async () => {
  try {
    projectList.value = await fetchArchiveProjectList()
    if (projectList.value.length > 0 && !activeProjectId.value) await handleSelectProject(projectList.value[0].id)
  } catch (error) {
    console.error('Failed to load archive project list:', error)
    ElMessage.error(error.message || '加载项目素材库失败')
  }
}
const loadInspirationList = async () => {
  try {
    inspirationList.value = await fetchInspirationList()
  } catch (error) {
    console.error('Failed to load inspiration list:', error)
    ElMessage.error(error.message || '加载灵感列表失败')
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
const handleCreateInspiration = async () => {
  if (!inspirationForm.value.title.trim()) {
    ElMessage.warning('请输入灵感标题')
    return
  }
  inspirationLoading.value = true
  try {
    await createInspiration(inspirationForm.value)
    inspirationForm.value = { title: '', sourcePlatform: '', sourceType: 'manual', category: '', tags: '', summary: '', hotValue: '' }
    await loadInspirationList()
    ElMessage.success('灵感已保存')
  } catch (error) {
    console.error('Failed to create inspiration:', error)
    ElMessage.error(error.message || '保存灵感失败')
  } finally {
    inspirationLoading.value = false
  }
}
const handleImportDemo = async () => {
  try {
    await importDemoInspirations()
    await loadInspirationList()
    ElMessage.success('示例热题材已导入')
  } catch (error) {
    console.error('Failed to import demo inspirations:', error)
    ElMessage.error(error.message || '导入示例热题材失败')
  }
}
const handleCreateProject = async (id) => {
  try {
    await createProjectFromInspiration(id)
    await loadProjectList()
    activeTab.value = 'archive'
    ElMessage.success('已转为项目，可继续生成分镜和视频')
  } catch (error) {
    console.error('Failed to create project from inspiration:', error)
    ElMessage.error(error.message || '转项目失败')
  }
}
onMounted(async () => {
  await loadProjectList()
  await loadInspirationList()
})
</script>

<style scoped>
.material-workbench { display: flex; flex-direction: column; gap: 20px; }
.card { min-height: 640px; }
.card-header { display: flex; justify-content: space-between; align-items: center; font-weight: 700; }
.detail-title { color: #409eff; }
.search-box { margin-bottom: 16px; }
.project-list, .inspiration-list { display: flex; flex-direction: column; gap: 12px; }
.project-item, .inspiration-item { padding: 12px; border: 1px solid #e4e7ed; border-radius: 10px; cursor: pointer; background: #fff; }
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
.inspiration-top { display: flex; justify-content: space-between; gap: 12px; }
</style>
<template>
  <div class="material-workbench">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="项目素材库" name="archive">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="card">
              <template #header>
                <div class="card-header">项目素材库</div>
              </template>

              <el-input
                v-model="keyword"
                clearable
                placeholder="搜索项目名、标签或来源标题"
                class="search-box"
              />

              <div class="project-list">
                <div
                  v-for="project in filteredProjects"
                  :key="project.id"
                  :class="['project-item', { active: project.id === activeProjectId }]"
                  @click="handleSelectProject(project.id)"
                >
                  <div class="project-title">{{ project.name }}</div>
                  <div class="project-meta">
                    <span>镜头 {{ project.shotCount || 0 }}</span>
                    <span>图片 {{ project.imageTaskCount || 0 }}</span>
                    <span>视频 {{ project.videoTaskCount || 0 }}</span>
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

              <div v-if="!detail.project" class="empty-state">
                先选择左侧项目，再查看镜头素材和任务记录。
              </div>

              <template v-else>
                <div class="detail-summary">
                  <div>来源：{{ detail.project.sourceTitle || '手动录入' }}</div>
                  <div>标签：{{ detail.project.genreTags || '未设置' }}</div>
                  <div>状态：{{ detail.project.status || 'draft' }}</div>
                </div>

                <el-tabs v-model="archiveTab">
                  <el-tab-pane label="镜头素材" name="shots">
                    <div class="shot-grid">
                      <div v-for="shot in detail.shotList" :key="shot.shotId" class="shot-card">
                        <el-image
                          v-if="shot.imageUrl"
                          :src="shot.imageUrl"
                          fit="cover"
                          class="shot-image"
                        />
                        <div class="shot-body">
                          <div class="shot-title">镜头 {{ shot.shotNo }}</div>
                          <div class="shot-text">{{ shot.actionDesc || '暂无动作描述' }}</div>
                          <div class="shot-prompt">{{ shot.promptText || '暂无提示词' }}</div>
                          <div class="shot-status">
                            <span>图片：{{ shot.imageStatus || 'pending' }}</span>
                            <span>视频：{{ shot.videoStatus || 'pending' }}</span>
                          </div>
                          <a
                            v-if="shot.videoUrl"
                            :href="shot.videoUrl"
                            target="_blank"
                            rel="noreferrer"
                            class="video-link"
                          >
                            查看视频结果
                          </a>
                        </div>
                      </div>
                    </div>
                  </el-tab-pane>

                  <el-tab-pane label="任务记录" name="tasks">
                    <el-table :data="detail.taskList" border style="width: 100%">
                      <el-table-column prop="id" label="任务 ID" width="100" />
                      <el-table-column prop="taskType" label="类型" width="120" />
                      <el-table-column prop="provider" label="提供商" width="140" />
                      <el-table-column prop="modelName" label="模型" width="160" />
                      <el-table-column prop="status" label="状态" width="120" />
                      <el-table-column
                        prop="resultUrl"
                        label="结果地址"
                        min-width="220"
                        show-overflow-tooltip
                      />
                    </el-table>
                  </el-tab-pane>
                </el-tabs>
              </template>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="灵感采集" name="inspiration">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-card class="card">
              <template #header>
                <div class="card-header">
                  <span>灵感池</span>
                  <el-button type="primary" link @click="handleImportDemo">导入示例热题材</el-button>
                </div>
              </template>

              <el-form :model="inspirationForm" label-width="88px">
                <el-form-item label="标题">
                  <el-input v-model="inspirationForm.title" placeholder="例如：末世小队求生手册" />
                </el-form-item>
                <el-form-item label="平台">
                  <el-input v-model="inspirationForm.sourcePlatform" placeholder="例如：番茄小说" />
                </el-form-item>
                <el-form-item label="分类">
                  <el-input v-model="inspirationForm.category" placeholder="例如：规则怪谈" />
                </el-form-item>
                <el-form-item label="标签">
                  <el-input v-model="inspirationForm.tags" placeholder="多个标签用逗号分隔" />
                </el-form-item>
                <el-form-item label="热度">
                  <el-input v-model="inspirationForm.hotValue" placeholder="例如：高热 / 上升" />
                </el-form-item>
                <el-form-item label="摘要">
                  <el-input
                    v-model="inspirationForm.summary"
                    type="textarea"
                    :rows="4"
                    placeholder="输入题材摘要，后续可一键转项目"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" :loading="inspirationLoading" @click="handleCreateInspiration">
                    保存灵感
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>

          <el-col :span="14">
            <el-card class="card">
              <template #header>
                <div class="card-header">已收集灵感</div>
              </template>

              <div class="inspiration-list">
                <div v-for="item in inspirationList" :key="item.id" class="inspiration-item">
                  <div class="inspiration-top">
                    <div>
                      <div class="project-title">{{ item.title }}</div>
                      <div class="project-meta">
                        <span>{{ item.sourcePlatform || '手动录入' }}</span>
                        <span>{{ item.category || '未分类' }}</span>
                        <span>{{ item.hotValue || '普通' }}</span>
                      </div>
                    </div>
                    <el-button type="primary" link @click="handleCreateProject(item.id)">
                      转为项目
                    </el-button>
                  </div>
                  <div class="project-tag">{{ item.tags || '未设置标签' }}</div>
                  <div class="shot-text">{{ item.summary || '暂无摘要' }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchArchiveProjectDetail, fetchArchiveProjectList } from '../api/archive'
import {
  createInspiration,
  createProjectFromInspiration,
  fetchInspirationList,
  importDemoInspirations
} from '../api/inspiration'

const activeTab = ref('archive')
const archiveTab = ref('shots')
const keyword = ref('')
const activeProjectId = ref(null)
const projectList = ref([])
const inspirationList = ref([])
const inspirationLoading = ref(false)
const inspirationForm = ref({
  title: '',
  sourcePlatform: '',
  sourceType: 'manual',
  category: '',
  tags: '',
  summary: '',
  hotValue: ''
})
const detail = ref({
  project: null,
  shotList: [],
  taskList: []
})

const filteredProjects = computed(() => {
  const text = keyword.value.trim().toLowerCase()
  if (!text) {
    return projectList.value
  }
  return projectList.value.filter((item) =>
    [item.name, item.genreTags, item.sourceTitle].some((value) =>
      String(value || '').toLowerCase().includes(text)
    )
  )
})

const loadProjectList = async () => {
  try {
    projectList.value = await fetchArchiveProjectList()
    if (projectList.value.length > 0 && !activeProjectId.value) {
      await handleSelectProject(projectList.value[0].id)
    }
  } catch (error) {
    console.error('Failed to load archive project list:', error)
    ElMessage.error(error.message || '加载项目素材库失败')
  }
}

const loadInspirationList = async () => {
  try {
    inspirationList.value = await fetchInspirationList()
  } catch (error) {
    console.error('Failed to load inspiration list:', error)
    ElMessage.error(error.message || '加载灵感列表失败')
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

const handleCreateInspiration = async () => {
  if (!inspirationForm.value.title.trim()) {
    ElMessage.warning('请输入灵感标题')
    return
  }

  inspirationLoading.value = true
  try {
    await createInspiration(inspirationForm.value)
    inspirationForm.value = {
      title: '',
      sourcePlatform: '',
      sourceType: 'manual',
      category: '',
      tags: '',
      summary: '',
      hotValue: ''
    }
    await loadInspirationList()
    ElMessage.success('灵感已保存')
  } catch (error) {
    console.error('Failed to create inspiration:', error)
    ElMessage.error(error.message || '保存灵感失败')
  } finally {
    inspirationLoading.value = false
  }
}

const handleImportDemo = async () => {
  try {
    await importDemoInspirations()
    await loadInspirationList()
    ElMessage.success('示例热题材已导入')
  } catch (error) {
    console.error('Failed to import demo inspirations:', error)
    ElMessage.error(error.message || '导入示例热题材失败')
  }
}

const handleCreateProject = async (id) => {
  try {
    await createProjectFromInspiration(id)
    await loadProjectList()
    activeTab.value = 'archive'
    ElMessage.success('已转为项目，可继续生成分镜、图片和视频')
  } catch (error) {
    console.error('Failed to create project from inspiration:', error)
    ElMessage.error(error.message || '转项目失败')
  }
}

onMounted(async () => {
  await loadProjectList()
  await loadInspirationList()
})
</script>

<style scoped>
.material-workbench {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  min-height: 640px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 700;
}

.detail-title {
  color: #2563eb;
}

.search-box {
  margin-bottom: 16px;
}

.project-list,
.inspiration-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.project-item,
.inspiration-item {
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  cursor: pointer;
  background: #fff;
}

.project-item.active {
  border-color: #2563eb;
  box-shadow: 0 0 0 1px rgba(37, 99, 235, 0.15);
}

.project-title {
  font-weight: 600;
  color: #1f2937;
}

.project-meta {
  display: flex;
  gap: 12px;
  margin-top: 8px;
  color: #4b5563;
  font-size: 13px;
  flex-wrap: wrap;
}

.project-tag {
  margin-top: 8px;
  color: #6b7280;
  font-size: 13px;
}

.empty-state {
  color: #6b7280;
  padding: 24px 0;
}

.detail-summary {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
  color: #4b5563;
  font-size: 14px;
  flex-wrap: wrap;
}

.shot-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.shot-card {
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  overflow: hidden;
  background: #fff;
}

.shot-image {
  width: 100%;
  height: 220px;
  display: block;
}

.shot-body {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.shot-title {
  font-weight: 600;
  color: #1f2937;
}

.shot-text,
.shot-prompt,
.shot-status {
  color: #4b5563;
  font-size: 13px;
  line-height: 1.5;
}

.shot-status {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.video-link {
  color: #2563eb;
  text-decoration: none;
  font-size: 13px;
}

.inspiration-top {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}
</style>
