package com.ravi.amigo.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ReportAgent {

    private static final String SYSTEM = """
            You create a report for the user based on user's findings provided to you. 
            Create a clear and concise report from the findings.Do not add facts that are not
            present in the findings;Do not call tools during
            this summarization step.
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
