package com.ravi.amigo.controller;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.ArgumentMatchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatControllerTest {

    @Test
    void usesVerifiedGoogleEmailAsConversationId() {
        ChatClient chatClient = mock(ChatClient.class);
        ChatClient.ChatClientRequestSpec request = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.AdvisorSpec advisorSpec = mock(ChatClient.AdvisorSpec.class);
        ChatClient.CallResponseSpec response = mock(ChatClient.CallResponseSpec.class);
        OAuth2User user = mock(OAuth2User.class);
        when(user.getAttribute("email")).thenReturn("Person@Example.com");
        when(user.getAttribute("email_verified")).thenReturn(Boolean.TRUE);
        when(chatClient.prompt("hello")).thenReturn(request);
        when(request.advisors(ArgumentMatchers.<Consumer<ChatClient.AdvisorSpec>>any())).thenAnswer(invocation -> {
            Consumer<ChatClient.AdvisorSpec> consumer = invocation.getArgument(0);
            consumer.accept(advisorSpec);
            return request;
        });
        when(request.call()).thenReturn(response);
        when(response.content()).thenReturn("Hi");

        String result = new ChatController(chatClient).sendMessage("hello", user);

        assertEquals("Hi", result);
        verify(advisorSpec).param(ChatMemory.CONVERSATION_ID, "person@example.com");
    }

    @Test
    void rejectsUnverifiedGoogleEmail() {
        ChatClient chatClient = mock(ChatClient.class);
        OAuth2User user = mock(OAuth2User.class);
        when(user.getAttribute("email")).thenReturn("person@example.com");
        when(user.getAttribute("email_verified")).thenReturn(Boolean.FALSE);

        assertThrows(ResponseStatusException.class,
                () -> new ChatController(chatClient).sendMessage("hello", user));

        verify(chatClient, never()).prompt("hello");
    }
}
