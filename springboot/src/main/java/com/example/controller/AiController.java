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

    static String apiKey = System.getenv("ARK_API_KEY");
    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    static Dispatcher dispatcher = new Dispatcher();
    static ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl("https://ark.cn-beijing.volces.com/api/v3/").apiKey(apiKey).build();

    @PostMapping("/ask")
    public Result ask(@RequestBody String question) {
        try {
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是一个专业的心理健康助手").build();
            final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(question).build();
            messages.add(systemMessage);
            messages.add(userMessage);

            BotChatCompletionRequest chatCompletionRequest = BotChatCompletionRequest.builder()
                    .botId("bot-20250410102443-mj9rx")
                    .messages(messages)
                    .build();

            BotChatCompletionResult chatCompletionResult = service.createBotChatCompletion(chatCompletionRequest);
            String answer = (String) chatCompletionResult.getChoices().get(0).getMessage().getContent();
            return Result.success(answer);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("请求火山引擎大模型AI 出错");
        }
    }
}