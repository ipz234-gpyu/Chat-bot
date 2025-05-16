package bot.infrastructure.telegram.command;

import bot.domain.Player;
import bot.domain.UserSession;
import bot.infrastructure.storage.PlayerRepository;
import bot.infrastructure.storage.SessionRepository;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.util.BotSessionManager;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        SessionRepository sessionRepository = new SessionRepository(update.getCallbackQuery().getFrom().getId());
        PlayerRepository playerRepository = new PlayerRepository(update.getCallbackQuery().getFrom().getId());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        BotSessionManager.setState(chatId, BotStateType.READY_TO_START);
        UserSession userSession = BotSessionManager.getSession(chatId);
        sessionRepository.save(userSession);

        Player player = new Player();
        player.setUsername(update.getCallbackQuery().getFrom().getUserName());
        playerRepository.save(player);

        editMessage(update,
                "\uD83E\uDDD9 Гру створено! Готові розпочати цю захоплюючу історію?",
                null);
    }

    @Override
    public void undo(Update update) {
        SessionRepository sessionRepository = new SessionRepository(update.getCallbackQuery().getFrom().getId());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.initSession(chatId, new UserSession(chatId));
        BotSessionManager.setState(chatId, BotStateType.SELECTING_SESSION);

        editMessage(update,
                "\uD83D\uDD01 Ви відмовилися від створення історії. Ось перелік ваших історій!",
                CreateKeyboardDirector.createSessionKeyboard(sessionRepository.getAll()));
    }
}