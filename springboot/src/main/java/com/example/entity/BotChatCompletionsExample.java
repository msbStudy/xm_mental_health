package com.example.entity;

import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionRequest;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * BotChatCompletionsExample 类用于演示如何使用 ArkService 进行聊天完成请求
 */
public class BotChatCompletionsExample {
    // 直接使用硬编码的 API Key
    static String apiKey = "6f6c971d-7ae4-477d-99a0-24ac062cfe4f";
    // 创建连接池
    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    // 创建调度器
    static Dispatcher dispatcher = new Dispatcher();
    // 构建 ArkService 实例
    static ArkService service = ArkService.builder()
            .dispatcher(dispatcher)
            .connectionPool(connectionPool)
            .baseUrl("https://ark.cn-beijing.volces.com/api/v3/")
            .apiKey(apiKey)
            .build();

    /**
     * 主函数，用于与用户进行交互并处理聊天请求
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // 获取并输出开场白
            printWelcomeMessage();

            while (true) {
                System.out.println("\n请输入你的问题（输入 'exit' 退出）：");
                String question = scanner.nextLine();

                if ("exit".equalsIgnoreCase(question)) {
                    break;
                }

                // 标准请求示例
                System.out.println("\n----- standard request -----");
                List<ChatMessage> messages = buildMessages(question);

                BotChatCompletionRequest chatCompletionRequest = BotChatCompletionRequest.builder()
                        .botId("bot-20250410102443-mj9rx")
                        .messages(messages)
                        .build();

                BotChatCompletionResult chatCompletionResult = service.createBotChatCompletion(chatCompletionRequest);
                chatCompletionResult.getChoices().forEach(choice -> System.out.println(choice.getMessage().getContent()));
                // the references example
                if (chatCompletionResult.getReferences() != null) {
                    chatCompletionResult.getReferences().forEach(ref -> System.out.println(ref.getUrl()));
                }

                // 流式请求示例
                System.out.println("\n----- streaming request -----");
                BotChatCompletionRequest streamChatCompletionRequest = BotChatCompletionRequest.builder()
                        .botId("bot-20250410102443-mj9rx")
                        .messages(messages)
                        .build();

                service.streamBotChatCompletion(streamChatCompletionRequest)
                        .doOnError(Throwable::printStackTrace)
                        .blockingForEach(
                                choice -> {
                                    if (choice.getReferences() != null && !choice.getReferences().isEmpty()) {
                                        choice.getReferences().forEach(ref -> System.out.println(ref.getUrl()));
                                    }
                                    if (!choice.getChoices().isEmpty()) {
                                        System.out.print(choice.getChoices().get(0).getMessage().getContent());
                                    }
                                }
                        );
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            // shutdown service after all requests is finished
            service.shutdownExecutor();
        }
    }

    /**
     * 构建聊天消息列表
     * @param question 用户输入的问题
     * @return 聊天消息列表
     */
    private static List<ChatMessage> buildMessages(String question) {
        List<ChatMessage> messages = new ArrayList<>();
        // 构建系统角色消息
        ChatMessage systemMessage = ChatMessage.builder()
                .role(ChatMessageRole.SYSTEM)
                .content("你是一个专业的心理健康助手,帮助用户解答心理健康问题")
                .build();
        // 构建用户角色消息
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .content(question)
                .build();
        messages.add(systemMessage);
        messages.add(userMessage);
        return messages;
    }

    /**
     * 打印开场白消息
     */
    private static void printWelcomeMessage() {
        List<ChatMessage> welcomeMessages = new ArrayList<>();
        // 构建系统角色消息
        ChatMessage systemMessage = ChatMessage.builder()
                .role(ChatMessageRole.SYSTEM)
                .content("你是一个专业的心理健康助手,帮助用户解答心理健康问题")
                .build();
        welcomeMessages.add(systemMessage);

        BotChatCompletionRequest welcomeRequest = BotChatCompletionRequest.builder()
                .botId("bot-20250410102443-mj9rx")
                .messages(welcomeMessages)
                .build();

        try {
            BotChatCompletionResult welcomeResult = service.createBotChatCompletion(welcomeRequest);
            welcomeResult.getChoices().forEach(choice -> System.out.println(choice.getMessage().getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
