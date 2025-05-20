package bot.infrastructure.openai;

import bot.util.BotConfig;
import com.google.genai.*;

public class GeminiClient {
    private static GeminiClient instance;
    public final Client client;

    private GeminiClient() {
        String apiKey = BotConfig.OPENAI_API_KEY;
        this.client = Client.builder().apiKey(apiKey).build();
    }

    public static synchronized GeminiClient getInstance() {
        if (instance == null) {
            instance = new GeminiClient();
        }
        return instance;
    }
}

