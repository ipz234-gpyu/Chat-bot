package bot.infrastructure.openai.models;

import bot.infrastructure.openai.GeminiClient;
import bot.util.BotConfig;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;

public class BaseModel {
    public final GeminiClient gemini;
    protected final String baseModel;
    protected GenerateContentConfig config;

    public BaseModel(GeminiClient client) {
        this.gemini = client;
        this.baseModel =  BotConfig.BASE_MODEL;
    }

    public GenerateContentConfig getConfig() {
        return config;
    }

    public void setConfig(GenerateContentConfig config) {
        this.config = config;
    }

    public String generateText(String prompt) {
        GenerateContentResponse response = gemini.client.models.generateContent(baseModel, prompt, null);
        return response.text();
    }
}