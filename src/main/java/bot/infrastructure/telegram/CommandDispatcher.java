package bot.infrastructure.telegram;

import bot.infrastructure.telegram.enums.BotStateType;
import bot.infrastructure.telegram.command.Interface.BotCommand;
import bot.util.BotSessionManager;
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
            if (cmd == null) return;

            switch (parts[1]) {
                case "CONFIRM" -> cmd.confirm(update);
                case "UNDO" -> cmd.undo(update);
                default -> cmd.execute(update);
            }
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText().trim();

            BotCommand cmd = commandMap.get(text);
            if (cmd != null) {
                cmd.execute(update);
                return;
            }

            BotStateType state = BotSessionManager.getState(chatId);
            if (state != null) {
                String stateKey = state.name();
                BotCommand stateCmd = commandMap.get(stateKey);
                if (stateCmd != null) {
                    stateCmd.execute(update);
                }
            }
        }
    }
}