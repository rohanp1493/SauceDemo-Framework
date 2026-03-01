package utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigReader;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiProvider implements AIProvider {

    private OkHttpClient client;
    private ObjectMapper mapper;
    private static final String GEMINI_URL =
        "https://generativelanguage.googleapis.com"
        + "/v1beta/models/{model}:generateContent"
        + "?key={apiKey}";

    public GeminiProvider() {
        client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
        mapper = new ObjectMapper();
    }

    @Override
    public String getProviderName() {
        return "Google Gemini";
    }

    @Override
    public String ask(
            String systemPrompt,
            String userMessage) {
        try {
            ConfigReader config =
                ConfigReader.getInstance();

            String apiKey = config.getOrDefault(
                "gemini.api.key", "");
            if (apiKey.isEmpty()) {
                return "Gemini API key not found";
            }

            String model = config.getOrDefault(
                "gemini.model", "gemini-2.0-flash");

            String url = GEMINI_URL
                .replace("{model}", model)
                .replace("{apiKey}", apiKey);

            String fullMessage =
                systemPrompt + "\n\n" + userMessage;

            String requestBody = "{"
                + "\"contents\": [{"
                + "\"parts\": [{"
                + "\"text\": \""
                + escapeJson(fullMessage)
                + "\"}]}]}";

            Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                    requestBody,
                    MediaType.parse(
                        "application/json")))
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
                return "Gemini Error: "
                    + json.path("error")
                        .path("message")
                        .asText();
            }

            return json
                .path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text")
                .asText("No response");

        } catch (Exception e) {
            return "Gemini error: "
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