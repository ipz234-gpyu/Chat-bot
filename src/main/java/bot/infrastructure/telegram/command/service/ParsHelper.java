package bot.infrastructure.telegram.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ParsHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String parseFromCallback(Update update) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
            String[] parts = update.getCallbackQuery().getData().split(":");
            return parts.length > 2 ? parts[2] : null;
        }
        return null;
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

    public static <T> String listToJson(List<T> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Помилка парсингу в JSON", e);
        }
    }
}
