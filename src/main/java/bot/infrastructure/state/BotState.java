package bot.infrastructure.state;

import bot.infrastructure.telegram.enums.BotStateType;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotState {
    void handle(Update update);
    BotState next();
    BotState previous();
    BotStateType getStateType();
}