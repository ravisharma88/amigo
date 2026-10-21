package com.ravi.amigo.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ChatAgent {

    public static final String SYSTEM_PROMPT = """
    You are a helpful and friendly assistant. Answer clearly and accurately. Use the news tools for current news, weather tools for current weather and forecasts, and soccer tools for current soccer information. Do not guess current events, conditions, scores, or schedules
    """;

    @Value("${amigo.agent.system-prompt:#{T(com.ravi.amigo.agent.ChatAgent).SYSTEM_PROMPT}}") String systemPrompt;

    private final ChatClient chatClient;

    private final Advisor chatMemoryAdvisor;

    public ChatAgent(ChatClient chatClient, Advisor chatMemoryAdvisor) {
        this.chatClient = chatClient;
        this.chatMemoryAdvisor = chatMemoryAdvisor;
    }

    public String chat(String message, String email) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .advisors(chatMemoryAdvisor)
                .advisors(advisors -> advisors.param(
                        ChatMemory.CONVERSATION_ID, email.toLowerCase(Locale.ROOT)))
                .call()
                .content();
    }
}
