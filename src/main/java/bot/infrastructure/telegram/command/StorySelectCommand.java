package bot.infrastructure.telegram.command;

import bot.domain.Story;
import bot.infrastructure.storage.StoryRepository;
import bot.infrastructure.telegram.command.*;
import bot.infrastructure.telegram.command.service.*;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.UUID;

public class StorySelectCommand extends AbstractCallbackCommand {
    protected StoryRepository storyRepository = new StoryRepository();

    public StorySelectCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Story story = storyRepository.getById(UUID.fromString(ParsHelper.parseFromCallback(update)));
        editMessage(update,
                "Ви вибрали сюжет **" + story.getTitle() + "**. Підтвердити вибір?",
                InlineKeyboardUtil.confirmationButtons("STORY"));
    }

    @Override
    public void confirm(Update update) {
        editMessage(update,
                "Сюжет підтверджено. Тепер оберіть персонажа:",
                ParsHelper.createCharactersKeyboard());
    }

    @Override
    public void undo(Update update) {
        editMessage(update,
                "Вибір сюжету скасовано. Оберіть сюжет:",
                ParsHelper.createStoriesKeyboard());
    }
}