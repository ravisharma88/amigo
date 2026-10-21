package com.ravi.amigo.controller;

import com.ravi.amigo.agent.ResearchAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final ChatClient chatClient;
    private final ResearchAgent researchAgent;

    public ReportController(ChatClient chatClient, ResearchAgent researchAgent) {
        this.chatClient = chatClient;
        this.researchAgent = researchAgent;
    }

    @PostMapping("/report")
    public String sendMessage(
            @RequestBody String message) {
        return researchAgent.createDailyResearchReport(message);
    }
}
