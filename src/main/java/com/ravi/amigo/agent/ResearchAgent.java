package com.ravi.amigo.agent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ResearchAgent {

    private final String SYSTEM = """
                        You are a researcher who gets current information using the weather tool.
                        Collect:
                        1. The weather forecast for the five calendar days starting from today.
                        
                        For each forecast day, include the date, conditions, and high/low temperatures when available. Also include any weather alert.
                        Use the tools to retrieve up-to-date information.State clearly when a tool cannot provide requested information.
                        Do not ask any further questions.
                        """;

    private final ChatClient chatClient;
    private final ReportAgent reportAgent;

    public ResearchAgent(ChatClient chatClient, ReportAgent reportAgent) {
        this.chatClient = chatClient;
        this.reportAgent = reportAgent;
    }

    public String createDailyResearchReport(String userText) {

        String conversationId = "research-" + UUID.randomUUID();

        String findings = chatClient.prompt()
                .user(userText)
                .system(SYSTEM)
                .advisors(advisors -> advisors.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();

        if (findings == null || findings.isBlank()) {
            throw new IllegalStateException("The research tools did not return any findings");
        }

        return reportAgent.createReport(findings, conversationId);
    }
}
