package com.example.controller;

import com.example.common.Result;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionRequest;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ai")
public class AiController {

    // 从环境变量中获取API密钥
    private static final String API_KEY = System.getenv("ARK_API_KEY");

    // 配置连接池
    private static final ConnectionPool CONNECTION_POOL = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    // 配置调度器
    private static final Dispatcher DISPATCHER = new Dispatcher();
    // 创建ArkService实例
    private static final ArkService SERVICE = ArkService.builder()
            .dispatcher(DISPATCHER)
            .connectionPool(CONNECTION_POOL)
            .baseUrl("https://ark.cn-beijing.volces.com/api/v3/")
            .apiKey(API_KEY)
            .build();

    @PostMapping("/ask")
    public Result ask(@RequestBody String question) {
        try {
            // 构建消息列表
            List<ChatMessage> messages = buildMessages(question);
            // 构建聊天完成请求
            BotChatCompletionRequest request = buildRequest(messages);
            // 发送请求并获取结果
            BotChatCompletionResult result = sendRequest(request);
            // 提取回答内容
            String answer = extractAnswer(result);
            return Result.success(answer);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("请求火山引擎大模型AI 出错");
        }
    }

    /**
     * 构建消息列表
     * @param question 用户输入的问题
     * @return 消息列表
     */
    private List<ChatMessage> buildMessages(String question) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = ChatMessage.builder()
                .role(ChatMessageRole.SYSTEM)
                .content("你是一个专业的心理健康助手")
                .build();
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .content(question)
                .build();
        messages.add(systemMessage);
        messages.add(userMessage);
        return messages;
    }

    /**
     * 构建聊天完成请求
     * @param messages 消息列表
     * @return 聊天完成请求
     */
    private BotChatCompletionRequest buildRequest(List<ChatMessage> messages) {
        return BotChatCompletionRequest.builder()
                .botId("bot-20250410102443-mj9rx")
                .messages(messages)
                .build();
    }

    /**
     * 发送聊天完成请求
     * @param request 聊天完成请求
     * @return 聊天完成结果
     * @throws Exception 请求过程中可能出现的异常
     */
    private BotChatCompletionResult sendRequest(BotChatCompletionRequest request) throws Exception {
        return SERVICE.createBotChatCompletion(request);
    }

    /**
     * 提取回答内容
     * @param result 聊天完成结果
     * @return 回答内容
     */
    private String extractAnswer(BotChatCompletionResult result) {
        return (String) result.getChoices().get(0).getMessage().getContent();
    }
}