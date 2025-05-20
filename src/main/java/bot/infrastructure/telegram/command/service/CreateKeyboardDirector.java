package bot.infrastructure.telegram.command.service;

import bot.domain.Character;
import bot.domain.Story;
import bot.domain.UserSession;
import bot.infrastructure.storage.CharacterRepository;
import bot.infrastructure.storage.StoryRepository;
import bot.util.InlineKeyboardButtonBuilder;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateKeyboardDirector {
    public static InlineKeyboardMarkup createStoriesKeyboard() {
        StoryRepository storyRepository = new StoryRepository();
        List<Story> stories = storyRepository.getAll();

        InlineKeyboardMarkup markup = InlineKeyboardUtil.createButtons(stories, "STORY");

        markup.getKeyboard().add(Collections.singletonList(
                InlineKeyboardButtonBuilder.create()
                        .text("➕ Створити всласну історію")
                        .callbackData("STORY_CREATE:default")
                        .build()
        ));

        return markup;
    }

    public static InlineKeyboardMarkup createCharactersKeyboard() {
        CharacterRepository characterRepository = new CharacterRepository();
        List<Character> characters = characterRepository.getAll();

        InlineKeyboardMarkup markup = InlineKeyboardUtil.createButtons(characters, "CHARACTER");

        markup.getKeyboard().add(Collections.singletonList(
                InlineKeyboardButtonBuilder.create()
                        .text("➕ Створити всласного компанйона")
                        .callbackData("CHARACTER_CREATE:default")
                        .build()
        ));

        return markup;
    }

    public static InlineKeyboardMarkup createSessionKeyboard(List<UserSession> userSessions) {
        InlineKeyboardMarkup markup = InlineKeyboardUtil.createButtons(userSessions, "SESSION");

        markup.getKeyboard().add(Collections.singletonList(
                InlineKeyboardButtonBuilder.create()
                        .text("➕ Створити нову історію")
                        .callbackData("SESSION_CREATE:default")
                        .build()
        ));

        return markup;
    }

    public static InlineKeyboardMarkup storyOptionsButtons(List<String> options) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            InlineKeyboardButton button = new InlineKeyboardButton(option);
            button.setCallbackData("PLAYING:d:" + i);
            rows.add(Collections.singletonList(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
}
