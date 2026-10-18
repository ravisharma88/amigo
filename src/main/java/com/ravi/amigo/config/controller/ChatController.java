package com.ravi.amigo.config.controller;

import com.openai.models.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @PostMapping("/answer")
    public String sendMessage(@RequestBody String message) {
        var prompt = chatClient.prompt(message);
        prompt.options(OpenAiChatOptions.builder().model(ChatModel.GPT_5_4.asString()));
        return prompt.call().content();
    }
}
