package bot.infrastructure.telegram.command.service;

import bot.domain.Character;
import bot.domain.Story;
import bot.infrastructure.storage.CharacterRepository;
import bot.infrastructure.storage.StoryRepository;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public class ParsHelper {
    public static String parseFromCallback(Update update) {
        String[] parts = update.getCallbackQuery().getData().split(":");
        return parts.length > 1 ? parts[2] : "невідомо";
    }

    public static InlineKeyboardMarkup createStoriesKeyboard() {
        StoryRepository storyRepository = new StoryRepository();
        List<Story> stories = storyRepository.getAll();
        return InlineKeyboardUtil.createButtonsForStories(stories);
    }

    public static InlineKeyboardMarkup createCharactersKeyboard() {
        CharacterRepository characterRepository = new CharacterRepository();
        List<Character> characters = characterRepository.getAll();
        return InlineKeyboardUtil.createButtonsForCharacters(characters);
    }
}
