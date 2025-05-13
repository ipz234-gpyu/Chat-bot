package bot.util;

import bot.domain.Story;
import bot.domain.Character;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

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

    public static InlineKeyboardMarkup createButtonsForStories(List<Story> stories) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Story story : stories) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(story.getTitle());
            button.setCallbackData("STORY:default:" + story.getId());

            rows.add(Collections.singletonList(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    public static InlineKeyboardMarkup createButtonsForCharacters(List<Character> characters) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Character character : characters) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(character.getName());
            button.setCallbackData("CHARACTER:default:" + character.getId());

            rows.add(Collections.singletonList(button));
        }

        return new InlineKeyboardMarkup(rows);
    }
}
