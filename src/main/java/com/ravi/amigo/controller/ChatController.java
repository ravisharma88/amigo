package com.ravi.amigo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @PostMapping("/chat")
    public String sendMessage(
            @RequestBody String message,
            @RequestParam String conversationId) {
        return chatClient.prompt(message)
                .advisors(advisors -> advisors.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
