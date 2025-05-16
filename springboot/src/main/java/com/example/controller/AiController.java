package com.example.controller;

import com.example.common.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiController {

    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    private static final String API_KEY = System.getenv("ARK_API_KEY");

    private static final String BOT_ID = "bot-20250410102443-mj9rx";

    /**
     * 调用火山引擎 API 获取回答
     *
     * @param params 包含用户问题的参数
     * @return AI 的回答
     */
    @PostMapping("/ask")
    public Result ask(@RequestBody Map<String, String> params) {
        String question = params.get("question");
        logger.info("收到用户   提问: {}", question);

        try {
            // 构造请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", BOT_ID); // 模型 ID
            requestBody.put("stream", false); // 不启用流式返回
            Map<String, Object> streamOptions = new HashMap<>();
            streamOptions.put("include_usage", true);
            requestBody.put("stream_options", streamOptions);

            // 构造消息内容
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messages.add(userMessage);
            requestBody.put("messages", messages);

            // 构造请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);

            // 发送 POST 请求
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://ark.cn-beijing.volces.com/api/v3/bots/chat/completions";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);


            // 解析响应
            Map responseBody = response.getBody();
            assert responseBody != null;
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            String answer = (String) choices.get(0).get("message");
            logger.info("火山引擎返回的 choices 内容: {}", choices);

            logger.info("火山引擎返回答案: {}", answer);
            return Result.success(answer);

        } catch (Exception e) {
            logger.error("调用火山引擎失败", e);
            return Result.error("AI 服务暂时不可用: " + e.getMessage());
        }
    }
}
