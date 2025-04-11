package com.example.controller;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.common.Result;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionRequest;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;  
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AI控制器类，处理与AI相关的HTTP请求
 */
@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiController {
    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    // 火山引擎API配置常量
    private static final String API_KEY = "6f6c971d-7ae4-477d-99a0-24ac062cfe4f";
    private static final String BOT_ID = "bot-20250410102443-mj9rx";

    // OKHttp连接池配置
    private static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    private static Dispatcher dispatcher = new Dispatcher();

    // 初始化ArkService实例
    static ArkService service = ArkService.builder()
            .dispatcher(dispatcher)
            .connectionPool(connectionPool)
            .baseUrl("https://ark.cn-beijing.volces.com/api/v3/")
            .apiKey(API_KEY)
            .build();

    /**
     * 处理用户提问的POST请求
     *
     * @param params 包含用户问题的参数
     * @param session 当前会话
     * @return AI的回答结果封装在Result对象中
     */
    @PostMapping("/ask")
    public Result ask(@RequestBody Map<String, String> params, HttpSession session) {
        try {
            // 1. 获取或初始化对话历史
            List<ChatMessage> messages = getSessionMessages(session);

            // 2. 添加用户消息
            messages.add(ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content(params.get("question"))
                    .build());

            // 3. 构建火山引擎请求
            BotChatCompletionRequest request = BotChatCompletionRequest.builder()
                    .botId(BOT_ID)
                    .messages(messages)
                    .temperature(0.7)  // 控制生成随机性
                    .maxTokens(1000)    // 最大响应长度
                    .build();

            // 4. 调用火山引擎API
            BotChatCompletionResult result = service.createBotChatCompletion(request);

            // 5. 处理响应
            String answer = result.getChoices().get(0).getMessage().getContent().toString();

            // 6. 保存对话历史
            messages.add(ChatMessage.builder()
                    .role(ChatMessageRole.ASSISTANT)
                    .content(answer)
                    .build());
            session.setAttribute("chatHistory", messages);

            return Result.success(answer);
        } catch (Exception e) {
            logger.error("火山引擎调用失败", e);
            return Result.error("AI服务暂时不可用：" + e.getMessage());
        }
    }

    /**
     * 从会话中获取对话历史，如果不存在则初始化
     *
     * @param session 当前会话
     * @return 对话历史列表
     */
    private List<ChatMessage> getSessionMessages(HttpSession session) {
        List<ChatMessage> messages = (List<ChatMessage>) session.getAttribute("chatHistory");
        if (messages == null) {
            messages = new ArrayList<>();
            // 系统初始化提示词
            messages.add(ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM)
                    .content("你是一个专业的心理健康助手，用中文以温暖、关怀的语气回答问题")
                    .build());
        }
        return messages;
    }

    // OKHttp客户端实例，用于HTTP请求
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
            .build();
}
