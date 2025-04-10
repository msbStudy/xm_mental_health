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
              <span style="margin-left: 5px;">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
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

    <!-- 中间的 AI 对话框部分 -->
    <div class="ai-dialog-container">
      <div class="ai-dialog">
        <div class="ai-dialog-header">
          <h2 class="ai-title">心理AI咨询</h2>
        </div>
        <div class="ai-dialog-content">
          <div v-if="answer" class="ai-answer">
            {{ answer }}
          </div>
        </div>
        <div class="ai-dialog-input">
          <el-input v-model="question" placeholder="请输入你的问题" @keyup.enter="sendQuestion"></el-input>
          <el-button @click="sendQuestion">发送</el-button>
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
          <!-- 可以添加更多底部信息 -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import router from '@/router/index.js';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
});

const question = ref('');
const answer = ref('');

const sendQuestion = async () => {
  if (question.value.trim() === '') return;

  try {
    const response = await axios.post("/ai/ask", { question: question.value });
    answer.value = response.data.data;
  } catch (error) {
    console.error('请求出错:', error);
    answer.value = '请求出错，请稍后重试';
    ElMessage.error('请求出错，请稍后重试');
  }
};

const navTo = (path) => {
  router.push(path);
};

const logout = () => {
  localStorage.removeItem('xm-user');
  router.push('/login');
};

if (!data.user.id) {
  logout();
  ElMessage.error('请登录！');
}
</script>

<style scoped>
/* 引入 front.css 中的样式 */
@import "@/assets/css/front.css";

.ai-dialog-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.ai-dialog {
  width: 600px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.ai-dialog-header {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  background-color: #f8f8f8;
}

.ai-dialog-content {
  padding: 20px;
  min-height: 300px;
  max-height: 400px;
  overflow-y: auto;
}

.ai-dialog-input {
  display: flex;
  padding: 10px;
  border-top: 1px solid #ccc;
}

.ai-dialog-input el-input {
  flex: 1;
  margin-right: 10px;
}

.ai-answer {
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 10px;
}
</style>