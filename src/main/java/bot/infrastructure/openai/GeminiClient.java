package bot.infrastructure.openai;

import com.google.genai.*;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class GeminiClient {
    private static GeminiClient instance;
    private final Client client;

    private GeminiClient() {
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("GOOGLE_API_KEY");
        this.client = Client.builder().apiKey(apiKey).build();
    }

    public static synchronized GeminiClient getInstance() {
        if (instance == null) {
            instance = new GeminiClient();
        }
        return instance;
    }

    public String generateText(String prompt) {
        GenerateContentResponse response =
                client.models.generateContent("gemini-2.0-flash-lite", prompt, null);
        return response.text();
    }
}

