<template>
  <div class="ai-module">
    <h2 class="ai-title">心理Ai咨询</h2>
    <input
        v-model="question"
        class="ai-input"
        placeholder="请输入你的问题"
    />
    <button
        @click="sendQuestion"
        class="ai-button"
    >
      发送
    </button>
    <div
        v-if="answer"
        class="ai-answer"
    >
      {{ answer }}
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import axios from 'axios';

const question = ref('');
const answer = ref('');

const sendQuestion = async () => {
  if (question.value.trim() === '') return;

  try {
    const response = await axios.post("/ai/ask", question.value);
    answer.value = response.data.data;
  } catch (error) {
    console.error('请求出错:', error);
    answer.value = '请求出错，请稍后重试';
  }
};
</script>

<style scoped>
/* 样式保持不变 */
</style>