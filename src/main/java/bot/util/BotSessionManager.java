package bot.util;

import bot.domain.UserSession;
import bot.infrastructure.telegram.enums.*;

import java.util.HashMap;
import java.util.Map;

public class BotSessionManager {
    private static final Map<Long, UserSession> sessions = new HashMap<>();

    public static UserSession getOrCreateSession(Long chatId) {
        return sessions.computeIfAbsent(chatId, UserSession::new);
    }

    public static void initSession(Long chatId, UserSession userSession) {
        sessions.put(chatId, userSession);
    }

    public static UserSession getSession(Long chatId) {
        return getOrCreateSession(chatId);
    }

    public static void setState(Long chatId, BotStateType state) {
        getOrCreateSession(chatId).setState(state);
    }

    public static BotStateType getState(Long chatId) {
        return getOrCreateSession(chatId).getState();
    }
}