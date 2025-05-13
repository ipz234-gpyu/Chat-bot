package bot.infrastructure.telegram.command;

import bot.infrastructure.telegram.command.Interface.BotCommand;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.ParsHelper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class StartCommand implements BotCommand {
    private final BotService botService;

    public StartCommand(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("Ласкаво просимо! Оберіть сюжет:");
        msg.setReplyMarkup(ParsHelper.createStoriesKeyboard());

        botService.sendMessage(msg);
    }
}