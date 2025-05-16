package bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineKeyboardButtonBuilder {
    private final InlineKeyboardButton button;

    public InlineKeyboardButtonBuilder() {
        this.button = new InlineKeyboardButton();
    }

    public static InlineKeyboardButtonBuilder create() {
        return new InlineKeyboardButtonBuilder();
    }

    public InlineKeyboardButtonBuilder text(String text) {
        button.setText(text);
        return this;
    }

    public InlineKeyboardButtonBuilder callbackData(String data) {
        button.setCallbackData(data);
        return this;
    }

    public InlineKeyboardButtonBuilder url(String url) {
        button.setUrl(url);
        return this;
    }

    public InlineKeyboardButtonBuilder switchInlineQuery(String query) {
        button.setSwitchInlineQuery(query);
        return this;
    }

    public InlineKeyboardButtonBuilder switchInlineQueryCurrentChat(String query) {
        button.setSwitchInlineQueryCurrentChat(query);
        return this;
    }

    public InlineKeyboardButton build() {
        return button;
    }
}
