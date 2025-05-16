package bot.infrastructure.telegram.command;

import bot.domain.UserSession;
import bot.infrastructure.storage.Interface.IRepository;
import bot.infrastructure.storage.Interface.ISessionRepository;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.infrastructure.storage.SessionRepository;
import bot.infrastructure.telegram.command.Interface.BotCommand;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.util.BotSessionManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

public class StartCommand implements BotCommand {
    private final BotService botService;

    public StartCommand(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void execute(Update update) {
        ISessionRepository sessionRepository = new SessionRepository(update.getMessage().getFrom().getId());
        Long chatId = update.getMessage().getChatId();
        BotSessionManager.initSession(chatId, new UserSession(chatId));

        SendMessage msg = new SendMessage();
        msg.setChatId(update.getMessage().getChatId().toString());

        msg.setText("\uD83E\uDDED Вітання, мандрівнику! На тебе чекає пригода — обери, з чого почнеться твоя історія.");
        List<UserSession> userSessions = sessionRepository.getAll();

        if (userSessions.isEmpty()) {
            BotSessionManager.setState(chatId, BotStateType.SELECTING_STORY);
            msg.setText("У вас ще немає збережених ігор. Розпочнемо нову!");
            msg.setReplyMarkup(CreateKeyboardDirector.createStoriesKeyboard());
        } else {
            BotSessionManager.setState(chatId, BotStateType.SELECTING_SESSION);
            msg.setText("Ось перелік ваших історій!");
            msg.setReplyMarkup(CreateKeyboardDirector.createSessionKeyboard(userSessions));
        }

        botService.sendMessage(msg);
    }
}