package bot.util;

import bot.domain.UserSession;
import bot.infrastructure.enums.*;

import java.util.HashMap;
import java.util.Map;

public class BotSessionManager {
    private static final Map<Long, UserSession> sessions = new HashMap<>();

    public static void initSession(Long chatId, UserSession userSession) {
        sessions.put(chatId, userSession);
    }

    public static UserSession getSession(Long chatId) {
        return sessions.get(chatId);
    }

    public static void setState(Long chatId, BotStateType state) {
        if (sessions.containsKey(chatId)) {
            sessions.get(chatId).setState(state);
        }
    }
}