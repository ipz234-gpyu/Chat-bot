package bot.infrastructure.telegram;

import bot.infrastructure.telegram.command.Interface.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<String, BotCommand> commandMap = new HashMap<>();

    public void register(String command, BotCommand handler) {
        commandMap.put(command, handler);
    }

    public void dispatch(Update update) {
        if (update.hasCallbackQuery()) {
            String[] parts = update.getCallbackQuery().getData().split(":");
            BotCommand cmd = commandMap.get(parts[0]);

            switch (parts[1]) {
                case "CONFIRM" -> cmd.confirm(update);
                case "UNDO" -> cmd.undo(update);
                default -> cmd.execute(update);
            }
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().trim().toLowerCase();
            BotCommand cmd = commandMap.get(text);
            if (cmd != null) {
                cmd.execute(update);
            }
        }
    }
}