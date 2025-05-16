package bot.util;

import bot.domain.Interface.Identifiable;
import bot.domain.Story;
import bot.domain.Character;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.function.Function;

public class InlineKeyboardUtil {
    public static InlineKeyboardMarkup confirmationButtons(String type) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        InlineKeyboardButton confirm = new InlineKeyboardButton("✅ Підтвердити");
        InlineKeyboardButton undo = new InlineKeyboardButton("🔙 Назад");

        confirm.setCallbackData(type.toUpperCase() + ":CONFIRM");
        undo.setCallbackData(type.toUpperCase() + ":UNDO");

        buttons.add(List.of(confirm, undo));

        return new InlineKeyboardMarkup(buttons);
    }

    public static <T extends Identifiable> InlineKeyboardMarkup createButtons(
            List<T> items,
            String prefix
    ) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (T item : items) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(item.toString());
            button.setCallbackData(prefix.toUpperCase() + ":default:" + item.getId().toString());
            rows.add(Collections.singletonList(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
}
