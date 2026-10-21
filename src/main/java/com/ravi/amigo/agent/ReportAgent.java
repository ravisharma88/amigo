package com.ravi.amigo.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ReportAgent {

    private static final String SYSTEM = """
            You summarize the findings provided to you by the user.Do not ask any further question.Do not call any tools.Dont any other fact apart from the details provided to you.
            """;

    private final ChatClient chatClient;

    public ReportAgent(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String createReport(String findings, String conversationId) {
        if (findings == null || findings.isBlank()) {
            throw new IllegalArgumentException("Research findings are required to create a report");
        }

        String report = chatClient.prompt()
                .user(findings)
                .system(SYSTEM)
                .advisors(advisors -> advisors.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();

        if (report == null || report.isBlank()) {
            throw new IllegalStateException("The chat client did not produce a research report");
        }
        return report;
    }
}
