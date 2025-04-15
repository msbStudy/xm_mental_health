<template>
  <div>
    <!-- 头部导航栏，样式参考 Front.vue -->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">心理健康预约系统</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/testPaper">心理测试</el-menu-item>
          <el-menu-item index="/front/doctor">心理医生</el-menu-item>
          <el-menu-item index="/front/propagate">心理健康宣传</el-menu-item>
          <el-menu-item index="/front/feedback">反馈与建议</el-menu-item>
          <el-menu-item index="/front/person">个人中心</el-menu-item>
          <!-- 其他菜单项 -->
          <el-menu-item index="/ai-module">心理AI咨询</el-menu-item>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer; height: 60px">
            <div style="display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px;">{{ data.user.name }}</span>
              <el-icon>
                <arrow-down/>
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="navTo('/front/reservation')">预约记录</el-dropdown-item>
                <el-dropdown-item @click="navTo('/front/testRecord')">测试记录</el-dropdown-item>
                <el-dropdown-item @click="navTo('/front/myFeedback')">我的反馈</el-dropdown-item>
                <el-dropdown-item @click="navTo('/front/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- AI对话框部分 -->
    <div class="ai-module-container">
      <div class="ai-dialog-wrapper">
        <div class="ai-dialog">
          <div class="dialog-header">
            <h2>心理AI咨询</h2>
            <el-button type="primary" size="small" @click="newChat" :disabled="loading">
              新对话
            </el-button>
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

    <!-- 底部黑色部分，样式和内容参考 Front.vue -->
    <div style="background-color: #3c3c3c;height: 400px;">
      <div style="width: 50%;margin: 0 auto;display: flex;color: #c2c2c2">
        <div style="flex: 1;margin-top: 20px">
          <div style="padding: 10px 0;font-size: 18px">心理健康预约系统</div>
          <div style="padding: 5px 0">上亿心理医生共同打造的"心理健康"</div>
          <div style="padding: 5px 0">60,000 多次心理咨询次数</div>
          <div style="padding: 5px 0">600,000 个细分分类</div>
          <div style="padding: 5px 0">760,000,000 次心理测试</div>
          <div style="padding: 5px 0">38,000+ 心理测试试卷</div>
        </div>
        <div style="flex: 1;margin-top: 20px">
          <div style="padding: 10px 0;font-size: 18px">关于我们</div>
          <div style="padding: 5px 0">关于心理健康预约系统联系我们</div>
          <div style="padding: 5px 0">隐私政策商标声明</div>
          <div style="padding: 5px 0">服务协议</div>
          <div style="padding: 5px 0">心理健康预约系统服务协议</div>
          <div style="padding: 5px 0">网络信息侵权通知指引</div>
          <div style="padding: 5px 0">心理健康预约系统服务监督员</div>
          <div style="padding: 5px 0">网站地图加入心理健康预约系统</div>
        </div>
        <div style="flex: 1;margin-top: 20px">
          <div style="padding: 10px 0;font-size: 18px">心理咨询服务</div>
          <div style="display: flex">
            <div style="flex: 1;padding: 10px 0">心理咨询</div>
            <div style="flex: 1;padding: 5px 0">心理检测</div>
          </div>
          <div style="display: flex">
            <div style="flex: 1;padding: 10px 0">医生问答</div>
            <div style="flex: 1;padding: 5px 0">心理答疑</div>
          </div>
          <div style="display: flex">
            <div style="flex: 1;padding: 10px 0">心理调节</div>
            <div style="flex: 1;padding: 5px 0">心理问卷</div>
          </div>
          <div style="display: flex">
            <div style="flex: 1;padding: 10px 0">APP下载</div>

          </div>
        </div>
      </div>
      <div style="width: 100%;text-align: center;color: #c2c2c2;margin-top: 40px;font-size: 20px">
        没有人是一座孤岛，每个人都需要关注心理健康
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElIcon } from 'element-plus'
import { Promotion, Loading } from '@element-plus/icons-vue'
import router from '@/router'
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
  try {
    loading.value = true;

    // 添加用户消息
    messages.value.push({
      role: 'user',
      content: question.value,
      timestamp: new Date().toLocaleTimeString()
    });

    const response = await axios.post('/ai/ask', {
      question: question.value
    }, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    // 处理成功响应
    if (response.data.code === 200) {
      messages.value.push({
        role: 'assistant',
        content: response.data.data,
        timestamp: new Date().toLocaleTimeString()
      });
    } else {
      throw new Error(response.data.msg || '服务异常');
    }
  } catch (error) {
    messages.value.push({
      role: 'error',
      content: `请求失败：${error.message}`,
      timestamp: new Date().toLocaleTimeString()
    });
  } finally {
    loading.value = false;
    question.value = '';
    scrollToBottom();
  }
};

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