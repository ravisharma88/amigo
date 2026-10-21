package com.ravi.amigo.controller;

import org.junit.jupiter.api.Test;
import com.ravi.amigo.agent.ChatAgent;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatControllerTest {

    @Test
    void forwardsMessageAndVerifiedGoogleEmailToChatAgent() {
        ChatAgent chatAgent = mock(ChatAgent.class);
        OAuth2User user = mock(OAuth2User.class);
        when(user.getAttribute("email")).thenReturn("Person@Example.com");
        when(user.getAttribute("email_verified")).thenReturn(Boolean.TRUE);
        when(chatAgent.chat("hello", "Person@Example.com")).thenReturn("Hi");

        String result = new ChatController(chatAgent).sendMessage("hello", user);

        assertEquals("Hi", result);
        verify(chatAgent).chat("hello", "Person@Example.com");
    }

    @Test
    void rejectsUnverifiedGoogleEmail() {
        ChatAgent chatAgent = mock(ChatAgent.class);
        OAuth2User user = mock(OAuth2User.class);
        when(user.getAttribute("email")).thenReturn("person@example.com");
        when(user.getAttribute("email_verified")).thenReturn(Boolean.FALSE);

        assertThrows(ResponseStatusException.class,
                () -> new ChatController(chatAgent).sendMessage("hello", user));

        verify(chatAgent, never()).chat("hello", "person@example.com");
    }
}
