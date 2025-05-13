package bot.infrastructure.telegram;

import bot.infrastructure.openai.GeminiClient;
import bot.domain.*;
import bot.infrastructure.storage.StoryRepository;
import bot.util.BotConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.File;
import java.util.List;

public class TelegramBotHandler extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final GeminiClient gemini;
    private final StoryRepository storyRepository = new StoryRepository();

    public TelegramBotHandler() {
        this.botUsername = "Roleplay StoryForge";
        this.botToken = BotConfig.TELEGRAM_BOT_TOKEN;
        this.gemini = GeminiClient.getInstance();
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                handleStartCommand(chatId);
            }
        }
    }

    private void handleStartCommand(Long chatId) {
        File userDir = new File("data/users/" + chatId);
        if (!userDir.exists()) {
            boolean created = userDir.mkdirs();
            if (!created) {
                sendMessage(chatId, "Не вдалося створити директорію користувача.");
                return;
            }
        }
        List<Story> stories = storyRepository.getAll();
        StringBuilder response = new StringBuilder("Оберіть історію:\n");
        for (Story story : stories) {
            response.append("- ").append(story.getTitle()).append("\n");
        }
        sendMessage(chatId, response.toString());
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}