package bot.infrastructure.telegram.command.Interface;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {
    void execute(Update update);
    default void confirm(Update update) {}
    default void undo(Update update) {}
}
