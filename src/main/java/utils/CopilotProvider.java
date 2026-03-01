package utils;

import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigReader;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CopilotProvider implements AIProvider {

    private OkHttpClient client;
    private ObjectMapper mapper;

    // GitHub Copilot uses OpenAI compatible API
    private static final String COPILOT_URL =
        "https://api.githubcopilot.com/chat/completions";

    public CopilotProvider() {
        client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
        mapper = new ObjectMapper();
    }

    @Override
    public String getProviderName() {
        return "GitHub Copilot";
    }

    @Override
    public String ask(
            String systemPrompt,
            String userMessage) {
        try {
            ConfigReader config =
                ConfigReader.getInstance();

            String apiKey = config.getOrDefault(
                "copilot.api.key", "");
            if (apiKey.isEmpty()) {
                return "Copilot API key not found";
            }

            String model = config.getOrDefault(
                "copilot.model", "gpt-4o");

            // OpenAI compatible format
            String requestBody = "{"
                + "\"model\": \"" + model + "\","
                + "\"messages\": ["
                + "{\"role\": \"system\","
                + "\"content\": \""
                + escapeJson(systemPrompt) + "\"},"
                + "{\"role\": \"user\","
                + "\"content\": \""
                + escapeJson(userMessage) + "\"}"
                + "]}";

            Request request = new Request.Builder()
                .url(COPILOT_URL)
                .post(RequestBody.create(
                    requestBody,
                    MediaType.parse(
                        "application/json")))
                .addHeader("Authorization",
                    "Bearer " + apiKey)
                .addHeader("content-type",
                    "application/json")
                .addHeader("Editor-Version",
                    "vscode/1.85.0")
                .addHeader("Copilot-Integration-Id",
                    "vscode-chat")
                .build();

            Response response =
                client.newCall(request).execute();
            String responseBody =
                response.body().string();

            JsonNode json =
                mapper.readTree(responseBody);

            if (json.has("error")) {
                return "Copilot Error: "
                    + json.path("error")
                        .path("message")
                        .asText();
            }

            // OpenAI response format
            return json
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText("No response");

        } catch (Exception e) {
            return "Copilot error: "
                + e.getMessage();
        }
    }

    private String escapeJson(String text) {
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}