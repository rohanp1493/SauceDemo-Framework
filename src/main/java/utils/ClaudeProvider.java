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

public class ClaudeProvider implements AIProvider {

    private OkHttpClient client;
    private ObjectMapper mapper;
    private static final String CLAUDE_URL =
        "https://api.anthropic.com/v1/messages";

    public ClaudeProvider() {
        client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
        mapper = new ObjectMapper();
    }

    @Override
    public String getProviderName() {
        return "Anthropic Claude";
    }

    @Override
    public String ask(
            String systemPrompt,
            String userMessage) {
        try {
            ConfigReader config =
                ConfigReader.getInstance();

            String apiKey = config.getOrDefault(
                "claude.api.key", "");
            if (apiKey.isEmpty()) {
                return "Claude API key not found";
            }

            String model = config.getOrDefault(
                "claude.model",
                "claude-3-5-haiku-20241022");

            String requestBody = "{"
                + "\"model\": \""
                + model + "\","
                + "\"max_tokens\": 1024,"
                + "\"system\": \""
                + escapeJson(systemPrompt) + "\","
                + "\"messages\": [{"
                + "\"role\": \"user\","
                + "\"content\": \""
                + escapeJson(userMessage)
                + "\"}]}";

            Request request = new Request.Builder()
                .url(CLAUDE_URL)
                .post(RequestBody.create(
                    requestBody,
                    MediaType.parse(
                        "application/json")))
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version",
                    "2023-06-01")
                .addHeader("content-type",
                    "application/json")
                .build();

            Response response =
                client.newCall(request).execute();
            String responseBody =
                response.body().string();

            JsonNode json =
                mapper.readTree(responseBody);

            if (json.has("error")) {
                return "Claude Error: "
                    + json.path("error")
                        .path("message")
                        .asText();
            }

            return json
                .path("content").get(0)
                .path("text")
                .asText("No response");

        } catch (Exception e) {
            return "Claude error: "
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