package com.ravi.amigo.controller;

import com.ravi.amigo.agent.ChatAgent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {

    private final ChatAgent chatAgent;

    public ChatController(ChatAgent chatAgent) {
        this.chatAgent = chatAgent;
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

        return chatAgent.chat(message, email);
    }
}
