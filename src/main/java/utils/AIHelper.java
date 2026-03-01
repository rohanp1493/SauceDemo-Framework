package utils;

import config.ConfigReader;

public class AIHelper {

    // ───────────────────────────────────────────────
    // getProvider()
    // Reads ai.provider from config
    // Returns correct provider automatically
    //
    // Change ai.provider=gemini in config.properties
    // → automatically uses Gemini
    //
    // Change ai.provider=claude
    // → automatically uses Claude
    // ───────────────────────────────────────────────
    private static AIProvider getProvider() {
        String provider = ConfigReader
            .getInstance()
            .getOrDefault("ai.provider", "gemini")
            .toLowerCase();

        System.out.println(
            "Using AI provider: " + provider);

        switch (provider) {
        case "claude":
            return new ClaudeProvider();
        case "copilot":
            return new CopilotProvider();
        case "none":
            return new NoAIProvider();
        case "gemini":
        default:
            return new GeminiProvider();
    }
    }

    // ───────────────────────────────────────────────
    // ask()
    // Main method - delegates to correct provider
    // ───────────────────────────────────────────────
    public static String ask(
            String systemPrompt,
            String userMessage) {

        AIProvider provider = getProvider();
        System.out.println("Asking "
            + provider.getProviderName()
            + "...");

        String response = provider.ask(
            systemPrompt, userMessage);

        System.out.println(
            provider.getProviderName()
            + " responded successfully");

        return response;
    }

    // ───────────────────────────────────────────────
    // analyzeFailure()
    // Analyzes test failure using configured provider
    // ───────────────────────────────────────────────
    public static String analyzeFailure(
            String testName,
            String errorMsg,
            String pageSource) {

        String systemPrompt =
            "You are a Selenium test automation expert. "
            + "Analyze test failures and provide "
            + "clear explanations in plain English. "
            + "Focus on: "
            + "1) What went wrong "
            + "2) Why it likely happened "
            + "3) How to fix it. "
            + "Keep response under 150 words.";

        String shortPageSource =
            pageSource.length() > 500
                ? pageSource.substring(0, 500) + "..."
                : pageSource;

        String userMessage =
            "Test that failed: " + testName + "\n"
            + "Error: " + errorMsg + "\n"
            + "Page HTML:\n" + shortPageSource
            + "\n\nExplain what went wrong "
            + "and how to fix it.";

        return ask(systemPrompt, userMessage);
    }
}