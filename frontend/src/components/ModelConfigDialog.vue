<template>
  <div class="model-config-container">
    <!-- 右下角设置按钮：点击打开大模型配置弹窗 -->
    <div class="settings-button" @click="dialogVisible = true">
      <el-icon><Setting /></el-icon>
    </div>

    <!-- 大模型配置列表弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="大模型配置"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="dialog-content">
        <!-- 搜索和操作栏：支持按模型名称搜索，添加新配置 -->
        <div class="toolbar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索模型名称"
            style="width: 200px"
            clearable
          />
          <el-button type="primary" @click="handleAdd">添加配置</el-button>
        </div>

        <!-- 配置列表表格 -->
        <el-table :data="filteredConfigs" style="width: 100%; margin-top: 20px" border>
          <!-- 模型名称列 -->
          <el-table-column prop="modelName" label="模型名称" width="150" />

          <!-- API地址列，超长显示省略号，悬停显示完整内容 -->
          <el-table-column prop="apiUrl" label="API地址" min-width="200" show-overflow-tooltip />

          <!-- 状态列：显示启用/禁用状态 -->
          <el-table-column prop="isEnabled" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.isEnabled === 1 ? 'success' : 'info'">
                {{ row.isEnabled === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>

          <!-- 默认标记列：仅显示默认模型 -->
          <el-table-column label="默认" width="80">
            <template #default="{ row }">
              <el-tag v-if="row.isDefault === 1" type="warning">默认</el-tag>
            </template>
          </el-table-column>

          <!-- 操作列：编辑、设为默认、删除 -->
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button link type="success" size="small" @click="handleSetDefault(row)" v-if="row.isDefault !== 1">设为默认</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <!-- 添加/编辑配置弹窗：用于新建或修改模型配置 -->
    <el-dialog
      v-model="formDialogVisible"
      :title="isEdit ? '编辑配置' : '添加配置'"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
        <!-- 模型名称 -->
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入模型名称" />
        </el-form-item>

        <!-- API地址 -->
        <el-form-item label="API地址" prop="apiUrl">
          <el-input v-model="form.apiUrl" placeholder="请输入API地址" />
        </el-form-item>

        <!-- API密钥：密码输入框，支持显示/隐藏 -->
        <el-form-item label="API密钥" prop="apiKey">
          <el-input v-model="form.apiKey" type="password" placeholder="请输入API密钥" show-password />
        </el-form-item>

        <!-- 模型版本 -->
        <el-form-item label="模型版本" prop="modelVersion">
          <el-input v-model="form.modelVersion" placeholder="请输入模型版本" />
        </el-form-item>

        <!-- 最大Token数：数字输入框，范围100-100000 -->
        <el-form-item label="最大Token" prop="maxTokens">
          <el-input-number v-model="form.maxTokens" :min="100" :max="100000" :step="100" />
        </el-form-item>

        <!-- 温度参数：滑动条，范围0-1 -->
        <el-form-item label="温度参数" prop="temperature">
          <el-slider v-model="form.temperature" :min="0" :max="1" :step="0.1" show-input />
        </el-form-item>

        <!-- 是否启用：开关控件 -->
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>

        <!-- 设为默认：开关控件 -->
        <el-form-item label="设为默认" prop="isDefault">
          <el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>

        <!-- 描述：多行文本框 -->
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>

      <!-- 弹窗底部按钮：取消和确定 -->
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 大模型配置组件
 * 功能：管理AI大模型的配置信息，包括添加、编辑、删除、设置默认模型等操作
 */
import { ref, computed, onMounted, watch } from 'vue'
import { Setting } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 定义组件可以触发的事件
const emit = defineEmits(['config-updated'])

// 是否显示配置列表弹窗
const dialogVisible = ref(false)

// 是否显示添加/编辑表单弹窗
const formDialogVisible = ref(false)

// 是否为编辑模式，true表示编辑，false表示添加
const isEdit = ref(false)

// 搜索关键词，用于过滤模型列表
const searchKeyword = ref('')

// 提交按钮加载状态，防止重复提交
const submitLoading = ref(false)

// 模型配置列表数据
const configs = ref([])

// 表单引用，用于表单验证
const formRef = ref(null)

/**
 * 表单数据模型
 * 用于存储大模型配置的各个字段
 */
const form = ref({
  id: null,                    // 配置ID，编辑时才有值
  modelName: '',               // 模型名称
  apiUrl: '',                  // API调用地址
  apiKey: '',                  // API密钥
  modelVersion: '',            // 模型版本号
  maxTokens: 4096,             // 最大Token数
  temperature: 0.7,            // 温度参数，控制生成随机性
  isEnabled: 1,                // 是否启用：1-启用，0-禁用
  isDefault: 0,                // 是否默认：1-默认，0-非默认
  description: ''              // 描述信息
})

/**
 * 表单验证规则
 * 定义各字段的校验规则和错误提示信息
 */
const formRules = {
  modelName: [{ required: true, message: '请输入模型名称', trigger: 'blur' }],
  apiUrl: [{ required: true, message: '请输入API地址', trigger: 'blur' }],
  apiKey: [{ required: true, message: '请输入API密钥', trigger: 'blur' }]
}

/**
 * 计算属性：根据搜索关键词过滤配置列表
 * 如果搜索框为空，返回全部配置；否则按模型名称模糊匹配
 */
const filteredConfigs = computed(() => {
  if (!searchKeyword.value) return configs.value
  return configs.value.filter(config =>
    config.modelName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

/**
 * 从后端加载模型配置列表
 * 调用 /api/model-config/list 接口获取数据
 */
const loadConfigs = async () => {
  try {
    // 调试模式：直接请求后端API（绕过Vite代理），以便在Network面板查看请求
    const response = await fetch('http://localhost:8081/api/model-config/list')
    const res = await response.json()
    if (res.code === 2001) {
      configs.value = res.data || []
    }
  } catch (error) {
    console.error('加载配置失败:', error)
    ElMessage.error('加载配置失败')
  }
}

/**
 * 打开发送添加配置弹窗
 * 重置表单数据为默认值，切换到添加模式
 */
const handleAdd = () => {
  isEdit.value = false
  form.value = {
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
  }
  formDialogVisible.value = true
}

/**
 * 打开编辑配置弹窗
 * @param {Object} row - 当前行的配置数据
 * 将数据填充到表单中，切换到编辑模式
 */
const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  formDialogVisible.value = true
}

/**
 * 设置指定模型为默认模型
 * @param {Object} row - 当前行的配置数据
 * 调用后端接口将对应ID的模型设为首选项
 */
const handleSetDefault = async (row) => {
  try {
    const response = await fetch(`http://localhost:8081/api/model-config/${row.id}/default`, {
      method: 'PUT'
    })
    const res = await response.json()
    if (res.code === 2001) {
      ElMessage.success('设置成功')
      // 本地更新：先取消所有默认标记，再设置当前行为默认
      configs.value.forEach(item => {
        item.isDefault = 0
      })
      row.isDefault = 1
      emit('config-updated')
    } else {
      ElMessage.error(res.msg || '设置失败')
    }
  } catch (error) {
    console.error('设置失败:', error)
    ElMessage.error('设置失败')
  }
}

/**
 * 删除指定模型配置
 * @param {Object} row - 当前行的配置数据
 * 弹出确认框，用户确认后调用后端接口删除
 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该配置吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await fetch(`http://localhost:8081/api/model-config/${row.id}`, {
      method: 'DELETE'
    })
    const res = await response.json()
    if (res.code === 2001) {
      ElMessage.success('删除成功')
      loadConfigs()
      emit('config-updated')
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    // 用户取消操作时不显示错误
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 提交表单：添加或更新配置
 * 根据isEdit标志判断是新增还是更新，调用相应接口
 */
const handleSubmit = async () => {
  try {
    // 表单验证
    await formRef.value.validate()
    submitLoading.value = true

    // 根据模式选择API和HTTP方法
    const url = isEdit.value ? `http://localhost:8081/api/model-config/${form.value.id}` : 'http://localhost:8081/api/model-config'
    const method = isEdit.value ? 'PUT' : 'POST'

    const response = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })

    const res = await response.json()
    if (res.code === 2001) {
      ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
      formDialogVisible.value = false
      loadConfigs()
      emit('config-updated')
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (error) {
    // 表单验证失败时error为false，不显示错误提示
    if (error !== false) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  } finally {
    // 无论成功或失败，都要关闭加载状态
    submitLoading.value = false
  }
}

// 监听弹窗打开事件，打开时刷新列表数据
watch(dialogVisible, (newVal) => {
  if (newVal) {
    loadConfigs()
  }
})
</script>

<style scoped>
/* 右下角悬浮设置按钮样式 */
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

/* 悬停效果 */
.settings-button:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.6);
}

/* 工具栏样式：搜索框和按钮水平排列 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
