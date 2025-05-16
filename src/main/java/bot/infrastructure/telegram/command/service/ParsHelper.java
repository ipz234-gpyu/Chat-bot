package bot.infrastructure.telegram.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ParsHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String parseFromCallback(Update update) {
        String[] parts = update.getCallbackQuery().getData().split(":");
        return parts.length > 1 ? parts[2] : "невідомо";
    }

    public static <T> T parseJson(Class<T> clazz, String json) {
        if (json.startsWith("```")) {
            json = json.replaceAll("(?s)```(json)?", "").trim();
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Помилка парсингу JSON", e);
        }
    }
}
