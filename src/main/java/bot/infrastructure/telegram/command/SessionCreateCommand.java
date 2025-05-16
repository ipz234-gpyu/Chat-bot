package bot.infrastructure.telegram.command;

import bot.domain.Player;
import bot.domain.UserSession;
import bot.infrastructure.storage.*;
import bot.infrastructure.storage.Interface.*;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.util.BotSessionManager;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class SessionCreateCommand extends AbstractCallbackCommand {
    public SessionCreateCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.setState(chatId, BotStateType.SELECTING_STORY);

        editMessage(update,
                "Розпочнемо нову захоплюючу історію!",
                CreateKeyboardDirector.createStoriesKeyboard());
    }

    @Override
    public void confirm(Update update) {
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        // 1. Отримання сесії
        UserSession userSession = BotSessionManager.getSession(chatId);
        BotSessionManager.setState(chatId, BotStateType.READY_TO_START);

        // 2. Ініціалізація репозиторіїв
        ISessionRepository sessionRepository = new SessionRepository(userId);
        IPlayerRepository playerRepository = new PlayerRepository(userId);
        IStoryTreeRepository storyTreeRepository = new StoryTreeRepository(userId);
        IStoryCharacterRepository storyCharacterRepository = new StoryCharacterRepository(userId);
        IHistoryRepository historyRepository = new HistoryRepository(userId);

        // 3. Збереження основної сесії
        sessionRepository.save(userSession);

        // 4. Створення гравця
        Player player = new Player(update.getCallbackQuery().getFrom().getLastName());
        player.setId(userSession.getId());
        playerRepository.save(player);

        // 5. Ініціалізація порожніх файлів
        storyTreeRepository.saveAll(List.of(), userSession.getId());
        storyCharacterRepository.saveAll(List.of(), userSession.getId());
        historyRepository.saveAll(List.of(), userSession.getId());

        // 6. Повідомлення
        editMessage(update,
                "\uD83E\uDDD9 Гру створено! Готові розпочати цю захоплюючу історію?",
                null);
    }

    @Override
    public void undo(Update update) {
        ISessionRepository sessionRepository = new SessionRepository(update.getCallbackQuery().getFrom().getId());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.initSession(chatId, new UserSession(chatId));
        BotSessionManager.setState(chatId, BotStateType.SELECTING_SESSION);

        editMessage(update,
                "\uD83D\uDD01 Ви відмовилися від створення історії. Ось перелік ваших історій!",
                CreateKeyboardDirector.createSessionKeyboard(sessionRepository.getAll()));
    }
}