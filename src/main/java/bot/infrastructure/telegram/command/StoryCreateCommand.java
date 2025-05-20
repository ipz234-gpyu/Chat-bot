package bot.infrastructure.telegram.command;

import bot.domain.Story;
import bot.infrastructure.openai.models.Interface.IStoryModel;
import bot.infrastructure.openai.models.StoryModel;
import bot.infrastructure.storage.Interface.IRepository;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.infrastructure.storage.StoryRepository;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.util.BotSessionManager;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StoryCreateCommand extends AbstractCallbackCommand {
    protected IRepository<Story> storyRepository = new StoryRepository();

    public StoryCreateCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.hasMessage() ? update.getMessage().getChatId()
                : update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null
                ? update.getCallbackQuery().getMessage().getChatId()
                : null;
        BotSessionManager.setState(chatId, BotStateType.STORY_CREATE);
        String text = update.hasMessage() ? update.getMessage().getText() : null;

        IStoryModel storyModel = new StoryModel();
        Story story = storyModel.generateStory(text);
        BotSessionManager.getSession(chatId).setSelectedStory(story);

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("""
                <b>📖 Опиши історію в якій би хотіли побувати, ось початкова опис історії:</b>
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
        ));
        msg.setReplyMarkup(InlineKeyboardUtil.confirmationButtons("STORY_CREATE"));

        botService.sendMessage(msg);
    }

    @Override
    public void confirm(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Story story = BotSessionManager.getSession(chatId).getSelectedStory();
        BotSessionManager.setState(chatId, BotStateType.SELECTING_CHARACTER);

        story = storyRepository.save(story);
        BotSessionManager.getSession(chatId).setSelectedStoryId(story.getId());

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
                "\uD83D\uDD01 Ти відмовився від створення історії — обери інший сюжет, де тобі судилося прославитися.",
                CreateKeyboardDirector.createStoriesKeyboard());
    }
}