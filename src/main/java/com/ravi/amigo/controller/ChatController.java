package com.ravi.amigo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/chat")
    public String sendMessage(
            @RequestBody String message,
            @AuthenticationPrincipal OAuth2User user) {
        String email = user.getAttribute("email");
        Boolean emailVerified = user.getAttribute("email_verified");
        if (email == null || email.isBlank() || !Boolean.TRUE.equals(emailVerified)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "A verified Google email address is required");
        }

        return chatClient.prompt(message)
                .advisors(advisors -> advisors.param(
                        ChatMemory.CONVERSATION_ID, email.toLowerCase(Locale.ROOT)))
                .call()
                .content();
    }
}
