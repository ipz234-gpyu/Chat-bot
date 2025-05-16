package bot.infrastructure.telegram;

import bot.infrastructure.openai.GeminiClient;
import bot.domain.*;
import bot.infrastructure.storage.*;
import bot.infrastructure.telegram.command.*;
import bot.infrastructure.telegram.command.service.BotService;
import bot.util.BotConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

public class TelegramBotHandler extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final GeminiClient gemini;
    private final CommandDispatcher dispatcher;
    private final BotService botService;

    private final StoryRepository storyRepository = new StoryRepository();
    private final CharacterRepository characterRepository = new CharacterRepository();

    private final Map<Long, UserSession> userSessions = new HashMap<>();

    public TelegramBotHandler() {
        this.botUsername = "Roleplay StoryForge";
        this.botToken = BotConfig.TELEGRAM_BOT_TOKEN;
        this.gemini = GeminiClient.getInstance();
        this.dispatcher = new CommandDispatcher();
        botService = new BotService(this);
        registerCommands();
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    private void registerCommands() {
        dispatcher.register("/start", new StartCommand(botService));
        dispatcher.register("SESSION", new SessionSelectCommand(botService));
        dispatcher.register("STORY", new StorySelectCommand(botService));
        dispatcher.register("CHARACTER", new CharacterSelectCommand(botService));
        dispatcher.register("SESSION_CREATE", new SessionCreateCommand(botService));
        dispatcher.register("STORY_CREATE", new StoryCreateCommand(botService));
        dispatcher.register("CHARACTER_CREATE", new CharacterCreateCommand(botService));
    }

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.dispatch(update);
    }
}