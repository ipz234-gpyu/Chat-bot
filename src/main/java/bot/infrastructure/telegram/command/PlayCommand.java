package bot.infrastructure.telegram.command;

import bot.domain.StoryTreeNode;
import bot.domain.UserSession;
import bot.infrastructure.openai.models.DungeonMaster;
import bot.infrastructure.storage.Interface.IStoryTreeRepository;
import bot.infrastructure.storage.StoryTreeRepository;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.command.service.ParsHelper;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.util.BotSessionManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PlayCommand extends AbstractCallbackCommand {
    DungeonMaster master = new DungeonMaster();

    public PlayCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Long userId = update.hasMessage() ? update.getMessage().getFrom().getId()
                : update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null
                ? update.getCallbackQuery().getFrom().getId()
                : null;
        Long chatId = update.hasMessage() ? update.getMessage().getChatId()
                : update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null
                ? update.getCallbackQuery().getMessage().getChatId()
                : null;

        BotSessionManager.setState(chatId, BotStateType.PLAYING);
        IStoryTreeRepository storyTreeRepository = new StoryTreeRepository(userId);

        UserSession session = BotSessionManager.getSession(chatId);

        String userResponse = null;
        if (update.hasMessage() && update.getMessage().getText() != null) {
            userResponse = update.getMessage().getText();
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
            userResponse = ParsHelper.parseFromCallback(update);
            StoryTreeNode node = storyTreeRepository.getLast(session.getId());
            if (node != null)
                userResponse = node.getOptions().get(Integer.parseInt(userResponse));
        }

        StoryTreeNode node = master.nextStep(userResponse, userId, session);
        storyTreeRepository.addNode(node, session.getId());

        StringBuilder text = new StringBuilder();

        if (node.getNpcResponse() != null && !node.getNpcResponse().isBlank()) {
            text.append("<b>🗣️ NPC відповідає:</b>\n");
            text.append("<i>").append(node.getNpcResponse()).append("</i>\n\n");
        }

        text.append("<b>📖 Сцена пригоди:</b>\n");
        text.append(node.getAction()).append("\n\n");

        text.append("<b>❔ Що ви зробите?</b>");

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text.toString());
        msg.setReplyMarkup(CreateKeyboardDirector.storyOptionsButtons(node.getOptions()));

        botService.sendMessage(msg);
    }
}
