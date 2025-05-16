package bot.infrastructure.telegram.command;

import bot.domain.UserSession;
import bot.infrastructure.storage.SessionRepository;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.util.BotSessionManager;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import bot.infrastructure.telegram.command.service.ParsHelper;

import java.util.UUID;

public class SessionSelectCommand extends AbstractCallbackCommand {
    public SessionSelectCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        SessionRepository sessionRepository = new SessionRepository(update.getCallbackQuery().getFrom().getId());
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

        editMessage(update,
                "⚔\uFE0F Готові продовжити цю захоплюючу історію?",
                null);
    }

    @Override
    public void undo(Update update) {
        SessionRepository sessionRepository = new SessionRepository(update.getCallbackQuery().getFrom().getId());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.initSession(chatId, new UserSession(chatId));
        BotSessionManager.setState(chatId, BotStateType.SELECTING_SESSION);

        editMessage(update,
                "\uD83C\uDFAD Гру ще не обрано… Обери істоію, в якій ти будеш подорожувати.",
                CreateKeyboardDirector.createSessionKeyboard(sessionRepository.getAll()));
    }
}