package bot.infrastructure.telegram;

import bot.infrastructure.openai.GeminiClient;
import bot.util.BotConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotHandler extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final GeminiClient gemini;

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
            String chatId = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();

            String result = gemini.generateText(userMessage);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(result);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}