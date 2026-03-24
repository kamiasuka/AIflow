<template>
  <div class="model-config-container">
    <div class="settings-button" @click="dialogVisible = true">
      <el-icon><Setting /></el-icon>
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="模型配置"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="dialog-content">
        <div class="toolbar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索模型名称"
            style="width: 220px"
            clearable
          />
          <el-button type="primary" @click="handleAdd">新增配置</el-button>
        </div>

        <el-table :data="filteredConfigs" style="width: 100%; margin-top: 20px" border>
          <el-table-column prop="modelName" label="模型名称" width="180" />
          <el-table-column prop="apiUrl" label="API 地址" min-width="240" show-overflow-tooltip />
          <el-table-column prop="isEnabled" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.isEnabled === 1 ? 'success' : 'info'">
                {{ row.isEnabled === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="默认" width="80">
            <template #default="{ row }">
              <el-tag v-if="row.isDefault === 1" type="warning">默认</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button
                v-if="row.isDefault !== 1"
                link
                type="success"
                size="small"
                @click="handleSetDefault(row)"
              >
                设为默认
              </el-button>
              <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <el-dialog
      v-model="formDialogVisible"
      :title="isEdit ? '编辑配置' : '新增配置'"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入模型名称" />
        </el-form-item>
        <el-form-item label="API 地址" prop="apiUrl">
          <el-input v-model="form.apiUrl" placeholder="请输入 API 地址" />
        </el-form-item>
        <el-form-item label="API Key" prop="apiKey">
          <el-input v-model="form.apiKey" type="password" placeholder="请输入 API Key" show-password />
        </el-form-item>
        <el-form-item label="模型版本" prop="modelVersion">
          <el-input v-model="form.modelVersion" placeholder="请输入模型版本" />
        </el-form-item>
        <el-form-item label="最大 Token" prop="maxTokens">
          <el-input-number v-model="form.maxTokens" :min="100" :max="100000" :step="100" />
        </el-form-item>
        <el-form-item label="Temperature" prop="temperature">
          <el-slider v-model="form.temperature" :min="0" :max="1" :step="0.1" show-input />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="设为默认" prop="isDefault">
          <el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { Setting } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createModelConfig,
  deleteModelConfig,
  fetchAllModelConfigs,
  setDefaultModelConfig,
  updateModelConfig
} from '../api/modelConfig'

const emit = defineEmits(['config-updated'])

const dialogVisible = ref(false)
const formDialogVisible = ref(false)
const isEdit = ref(false)
const searchKeyword = ref('')
const submitLoading = ref(false)
const configs = ref([])
const formRef = ref(null)

const createEmptyForm = () => ({
  id: null,
  modelName: '',
  apiUrl: '',
  apiKey: '',
  modelVersion: '',
  maxTokens: 4096,
  temperature: 0.7,
  isEnabled: 1,
  isDefault: 0,
  description: ''
})

const form = ref(createEmptyForm())

const formRules = {
  modelName: [{ required: true, message: '请输入模型名称', trigger: 'blur' }],
  apiUrl: [{ required: true, message: '请输入 API 地址', trigger: 'blur' }],
  apiKey: [{ required: true, message: '请输入 API Key', trigger: 'blur' }]
}

const filteredConfigs = computed(() => {
  if (!searchKeyword.value) {
    return configs.value
  }

  return configs.value.filter((config) =>
    config.modelName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

const loadConfigs = async () => {
  try {
    configs.value = await fetchAllModelConfigs()
  } catch (error) {
    console.error('Failed to load configs:', error)
    ElMessage.error(error.message || '加载配置失败')
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = createEmptyForm()
  formRef.value?.clearValidate()
  formDialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  formRef.value?.clearValidate()
  formDialogVisible.value = true
}

const handleSetDefault = async (row) => {
  try {
    await setDefaultModelConfig(row.id)
    ElMessage.success('设置成功')
    await loadConfigs()
    emit('config-updated')
  } catch (error) {
    console.error('Failed to set default config:', error)
    ElMessage.error(error.message || '设置失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条模型配置吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteModelConfig(row.id)
    ElMessage.success('删除成功')
    await loadConfigs()
    emit('config-updated')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete config:', error)
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true

    if (isEdit.value) {
      await updateModelConfig(form.value.id, form.value)
    } else {
      await createModelConfig(form.value)
    }

    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    formDialogVisible.value = false
    await loadConfigs()
    emit('config-updated')
  } catch (error) {
    if (error !== false) {
      console.error('Failed to submit config:', error)
      ElMessage.error(error.message || '操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}

watch(dialogVisible, (visible) => {
  if (visible) {
    loadConfigs()
  }
})

onMounted(() => {
  loadConfigs()
})
</script>

<style scoped>
.settings-button {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  background-color: #409eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.4);
  transition: all 0.3s;
}

.settings-button:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.6);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-content {
  min-height: 240px;
}
</style>