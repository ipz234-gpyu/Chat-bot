package bot.infrastructure.telegram.command.Interface;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface BotService {
    void sendMessage(SendMessage message);
    void sendEditMessage(EditMessageText editMessage);
}