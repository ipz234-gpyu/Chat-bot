package bot.infrastructure.telegram.command;

import bot.domain.Story;
import bot.infrastructure.storage.StoryRepository;
import bot.infrastructure.telegram.command.service.*;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.util.BotSessionManager;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;

public class StorySelectCommand extends AbstractCallbackCommand {
    protected StoryRepository storyRepository = new StoryRepository();

    public StorySelectCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Story story = storyRepository.getById(UUID.fromString(ParsHelper.parseFromCallback(update)));
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.getSession(chatId).setSelectedStory(story);
        BotSessionManager.setState(chatId, BotStateType.CONFIRMING_STORY);

        String storyMessage = """
                <b>📖 Обраний сюжет:</b>
                %s
                <b>🏷️ Назва:</b> <i>%s</i>
                <b>🔍 Опис пригоди:</b> %s
                <b>📌 Теги:</b> %s
                %s
                <b>❓ Підтвердити вибір цього сюжету?</b>
                """.formatted(
                "━".repeat(20),
                story.getTitle(),
                story.getDescription(),
                String.join(", ", story.getTags()),
                "━".repeat(20)
        );
        editMessage(update,
                storyMessage,
                InlineKeyboardUtil.confirmationButtons("STORY"));
    }

    @Override
    public void confirm(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.getSession(chatId).setSelectedStoryId(BotSessionManager.getSession(chatId).getSelectedStory().getId());
        BotSessionManager.setState(chatId, BotStateType.SELECTING_CHARACTER);

        editMessage(update,
                "\uD83E\uDDD9 Сюжет обрано! Тепер настав час вибрати героя з яким ти будеш подорожувати:",
                CreateKeyboardDirector.createCharactersKeyboard());
    }

    @Override
    public void undo(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.getSession(chatId).setSelectedStory(null);
        BotSessionManager.setState(chatId, BotStateType.SELECTING_STORY);

        editMessage(update,
                "\uD83D\uDD01 Поворот долі! Ти відмовився від шляху — обери інший сюжет, де тобі судилося прославитися.",
                CreateKeyboardDirector.createStoriesKeyboard());
    }
}