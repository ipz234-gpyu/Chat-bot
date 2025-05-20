package bot.infrastructure.telegram.command;

import bot.domain.Character;
import bot.infrastructure.openai.models.CharacterModel;
import bot.infrastructure.openai.models.Interface.ICharacterModel;
import bot.infrastructure.storage.Interface.IRepository;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.infrastructure.openai.GeminiClient;
import bot.infrastructure.storage.CharacterRepository;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.service.ParsHelper;
import bot.util.BotSessionManager;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CharacterCreateCommand extends AbstractCallbackCommand {
    protected IRepository<Character> characterRepository = new CharacterRepository();

    public CharacterCreateCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.hasMessage() ? update.getMessage().getChatId()
                : update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null
                ? update.getCallbackQuery().getMessage().getChatId()
                : null;
        BotSessionManager.setState(chatId, BotStateType.CHARACTER_CREATE);
        String text = update.hasMessage() ? update.getMessage().getText() : null;

        ICharacterModel characterModel = new CharacterModel();
        Character character = characterModel.generateCharacter(text);
        BotSessionManager.getSession(chatId).setSelectedCharacter(character);

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("""
                <b>🧙 Обраний персонаж:</b>
                %s
                <b>🏷️ Ім'я:</b> <i>%s</i>
                <b>📝 Опис:</b> %s
                %s
                <b>❓ Підтвердити вибір цього персонажа?</b>
                """.formatted(
                "━".repeat(20),
                character.getName(),
                character.getDescription(),
                "━".repeat(20)
        ));
        msg.setReplyMarkup(InlineKeyboardUtil.confirmationButtons("CHARACTER_CREATE"));

        botService.sendMessage(msg);
    }

    @Override
    public void confirm(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Character character = BotSessionManager.getSession(chatId).getSelectedCharacter();
        BotSessionManager.setState(chatId, BotStateType.READY_TO_START);

        character = characterRepository.save(character);
        BotSessionManager.getSession(chatId).setSelectedCharacterId(character.getId());

        editMessage(update,
                "⚔\uFE0F Твій напарник готовий до битви! Підтвердити створення нової гри?",
                InlineKeyboardUtil.confirmationButtons("SESSION_CREATE"));
    }

    @Override
    public void undo(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.getSession(chatId).setSelectedCharacter(null);
        BotSessionManager.setState(chatId, BotStateType.SELECTING_CHARACTER);

        editMessage(update,
                "\uD83C\uDFAD Напарника ще не обрано… Обери героя, з яким ти будеш подорожувати.",
                CreateKeyboardDirector.createCharactersKeyboard());
    }
}