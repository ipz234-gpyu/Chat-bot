package bot.infrastructure.telegram.command;

import bot.domain.Story;
import bot.domain.Character;
import bot.domain.UserSession;
import bot.infrastructure.storage.CharacterRepository;
import bot.infrastructure.storage.Interface.IRepository;
import bot.infrastructure.storage.Interface.ISessionRepository;
import bot.infrastructure.storage.SessionRepository;
import bot.infrastructure.storage.StoryRepository;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.infrastructure.telegram.command.service.ParsHelper;
import bot.util.BotSessionManager;
import bot.util.InlineKeyboardButtonBuilder;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SessionSelectCommand extends AbstractCallbackCommand {
    public SessionSelectCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        ISessionRepository sessionRepository = new SessionRepository(update.getCallbackQuery().getFrom().getId());
        UserSession userSession  = sessionRepository.getById(UUID.fromString(ParsHelper.parseFromCallback(update)));
        BotSessionManager.initSession(chatId, userSession);
        BotSessionManager.setState(chatId, BotStateType.CONFIRMING_SESSION);

        String characterMessage = """
                <b>🧙 Обраний історію:</b>
                %s
                <b>🏷️ Ім'я:</b> <i>%s</i>
                %s
                <b>❓ Підтвердити вибір цього персонажа?</b>
                """.formatted(
                "━".repeat(20),
                userSession.toString(),
                "━".repeat(20)
        );

        editMessage(update,
                characterMessage,
                InlineKeyboardUtil.confirmationButtons("SESSION"));
    }

    @Override
    public void confirm(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.setState(chatId, BotStateType.READY_TO_START);

        UserSession session = BotSessionManager.getSession(chatId);
        IRepository<Story> storyRepository = new StoryRepository();
        IRepository<Character> characterRepository = new CharacterRepository();

        session.setSelectedStory(storyRepository.getById(session.getSelectedStoryId()));
        session.setSelectedCharacter(characterRepository.getById(session.getSelectedCharacterId()));

        InlineKeyboardButton button =
                InlineKeyboardButtonBuilder.create()
                        .text("РОЗПОЧАТИ!")
                        .callbackData("PLAYING:default")
                        .build();

        List<InlineKeyboardButton> row = Collections.singletonList(button);
        List<List<InlineKeyboardButton>> rows = Collections.singletonList(row);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rows);

        editMessage(update,
                "⚔\uFE0F Готові продовжити цю захоплюючу історію?",
                markup);
    }

    @Override
    public void undo(Update update) {
        ISessionRepository sessionRepository = new SessionRepository(update.getCallbackQuery().getFrom().getId());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.initSession(chatId, new UserSession(chatId));
        BotSessionManager.setState(chatId, BotStateType.SELECTING_SESSION);

        editMessage(update,
                "\uD83C\uDFAD Гру ще не обрано… Обери істоію, в якій ти будеш подорожувати.",
                CreateKeyboardDirector.createSessionKeyboard(sessionRepository.getAll()));
    }
}