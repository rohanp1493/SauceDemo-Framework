package utils;

public interface AIProvider {
	
	// Every AI provider must implement this method
    // Takes a prompt and returns AI response
    String ask(String systemPrompt,
               String userMessage);

    // Get provider name for logging
    String getProviderName();

}
