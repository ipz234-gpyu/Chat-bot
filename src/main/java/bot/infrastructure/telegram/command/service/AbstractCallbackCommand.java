package bot.infrastructure.telegram.command.service;

import bot.infrastructure.telegram.command.Interface.BotCommand;
import bot.infrastructure.telegram.command.Interface.BotService;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public abstract class AbstractCallbackCommand implements BotCommand {
    protected final BotService botService;

    public AbstractCallbackCommand(BotService botService) {
        this.botService = botService;
    }

    /** Основна логіка команди (при первинному виклику). */
    @Override
    public abstract void execute(Update update);

    /** Підтвердження дії (при натисканні «Підтвердити»). */
    public void confirm(Update update) { }

    /** Відміна дії (при натисканні «Скасувати»). */
    public void undo(Update update) { }

    /** Допоміжний метод: змінює текст повідомлення. */
    protected void editMessage(Update update, String text) {
        editMessage(update, text, null);
    }
    /** Допоміжний метод: змінює текст повідомлення з Inline-клавіатурою. */
    protected void editMessage(Update update, String text, InlineKeyboardMarkup markup) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        EditMessageText edit = new EditMessageText();

        edit.setChatId(chatId.toString());
        edit.setMessageId(messageId);
        edit.setText(text);

        if (markup != null) {
            edit.setReplyMarkup(markup);
        }

        botService.sendEditMessage(edit);
    }
}