<template>
  <div>
    <!-- AI对话框部分 -->
    <div class="ai-module-container">
      <div class="ai-dialog-wrapper">
        <div class="ai-dialog">
          <div class="dialog-header">
            <h2>心理AI咨询</h2>
          </div>
          <div class="dialog-content" ref="chatContainer">
            <!-- 消息历史记录 -->
            <div v-for="(msg, index) in messages" :key="index"
                 :class="['message', msg.role]">
              <div class="message-bubble">
                {{ msg.content }}
              </div>
            </div>
            <!-- 加载状态指示 -->
            <div v-if="loading" class="loading-indicator">
              <el-icon class="is-loading"><Loading /></el-icon>
              <span>AI正在思考中...</span>
            </div>
          </div>
          <!-- 输入区域 -->
          <div class="dialog-input">
            <el-input
                v-model="question"
                placeholder="请输入您的心理问题（按Enter发送）"
                :disabled="loading"
                @keyup.enter="sendQuestion"
                clearable
            >
              <template #append>
                <el-button
                    type="primary"
                    @click="sendQuestion"
                    :disabled="!question.trim() || loading"
                    :icon="Promotion"
                />
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElIcon } from 'element-plus'
import { Promotion, Loading } from '@element-plus/icons-vue'
import router from '@/router/index.js'
import request from '@/utils/request' // 假设你的request文件在这里

import axios from 'axios'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})
// AI对话相关逻辑
const question = ref('')
const messages = ref([])
const loading = ref(false)
const chatContainer = ref(null)

const sendQuestion = async () => {
  if (!question.value.trim()) return;
  loading.value = true

  const token = localStorage.getItem('token')
  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: question.value,
    timestamp: new Date().toLocaleTimeString()
  })

  try {
    const res = await request.post('/ai/ask', { question: question.value }, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (res.code === '200') {
      messages.value.push({
        role: 'assistant',
        content: res.data,
        timestamp: new Date().toLocaleTimeString()
      })
    } else {
      ElMessage.error(res.msg || '服务异常')
      messages.value.push({
        role: 'error',
        content: `请求失败：${res.msg || '服务异常'}`,
        timestamp: new Date().toLocaleTimeString()
      })
    }
  } catch (error) {
    ElMessage.error(error.message || 'AI服务异常')
    messages.value.push({
      role: 'error',
      content: `请求失败：${error.message}`,
      timestamp: new Date().toLocaleTimeString()
    })
  } finally {
    loading.value = false
    question.value = ''
    scrollToBottom()
  }
}


const newChat = async () => {
  try {
    await axios.post('/ai/new')
    messages.value = []
    ElMessage.success('已开启新对话')
  } catch (error) {
    ElMessage.error('重置对话失败')
  }
}

const scrollToBottom = () => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

onMounted(() => {
  if (!data.user.id) {
    ElMessage.error('请先登录')
    router.push('/login')
  }
  scrollToBottom()
})
</script>

<style scoped>
/* 保持原有整体样式 */
@import "@/assets/css/front.css";

/* 新增AI对话框样式 */
.ai-module-container {
  padding: 30px 20px;
  min-height: calc(100vh - 500px);
}

.ai-dialog-wrapper {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
}

.dialog-header h2 {
  margin: 0;
  font-size: 20px;
  color: #303133;
}

.dialog-content {
  height: 500px;
  padding: 16px;
  overflow-y: auto;
  background: #f8f9fa;
}

.message {
  margin: 12px 0;
  display: flex;
}

.message.user {
  justify-content: flex-end;
}

.message.assistant {
  justify-content: flex-start;
}

.message-bubble {
  max-width: 75%;
  padding: 12px 16px;
  border-radius: 8px;
  line-height: 1.6;
  font-size: 14px;
  word-break: break-word;
}

.message.user .message-bubble {
  background: #409eff;
  color: white;
}

.message.assistant .message-bubble {
  background: #fff;
  color: #303133;
  border: 1px solid #ebeef5;
}

.message.error .message-bubble {
  background: #fef0f0;
  color: #f56c6c;
  border: 1px solid #fbc4c4;
}

.loading-indicator {
  padding: 12px;
  text-align: center;
  color: #909399;
}

.loading-indicator .el-icon {
  margin-right: 8px;
  animation: rotating 2s linear infinite;
}

.dialog-input {
  padding: 16px;
  border-top: 1px solid #ebeef5;
}

@keyframes rotating {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>