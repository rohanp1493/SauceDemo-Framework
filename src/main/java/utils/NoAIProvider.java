package utils;

public class NoAIProvider implements AIProvider {

    @Override
    public String getProviderName() {
        return "None";
    }

    @Override
    public String ask(
            String systemPrompt,
            String userMessage) {
        return "AI analysis disabled. "
            + "Set ai.provider=gemini or claude "
            + "in config.properties to enable.";
    }
}