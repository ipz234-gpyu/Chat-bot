package bot.infrastructure.telegram.command;

import bot.domain.Character;
import bot.infrastructure.storage.CharacterRepository;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.infrastructure.telegram.command.service.CreateKeyboardDirector;
import bot.infrastructure.telegram.enums.BotStateType;
import bot.util.BotSessionManager;
import bot.util.InlineKeyboardButtonBuilder;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import bot.infrastructure.telegram.command.service.ParsHelper;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Collections;
import java.util.UUID;

public class CharacterSelectCommand extends AbstractCallbackCommand {
    protected CharacterRepository characterRepository = new CharacterRepository();

    public CharacterSelectCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Character character = characterRepository.getById(UUID.fromString(ParsHelper.parseFromCallback(update)));
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.getSession(chatId).setSelectedCharacter(character);
        BotSessionManager.setState(chatId, BotStateType.CONFIRMING_CHARACTER);

        String characterMessage = """
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
        );

        editMessage(update,
                characterMessage,
                InlineKeyboardUtil.confirmationButtons("CHARACTER")
               );
    }

    @Override
    public void confirm(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotSessionManager.setState(chatId, BotStateType.READY_TO_START);
        BotSessionManager.getSession(chatId).setSelectedCharacterId(BotSessionManager.getSession(chatId).getSelectedCharacter().getId());

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