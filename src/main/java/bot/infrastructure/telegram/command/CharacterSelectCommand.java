package bot.infrastructure.telegram.command;

import bot.domain.Character;
import bot.infrastructure.storage.CharacterRepository;
import bot.infrastructure.telegram.command.service.AbstractCallbackCommand;
import bot.infrastructure.telegram.command.Interface.BotService;
import bot.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import bot.infrastructure.telegram.command.service.ParsHelper;

import java.util.UUID;

public class CharacterSelectCommand extends AbstractCallbackCommand {
    protected CharacterRepository characterRepository = new CharacterRepository();

    public CharacterSelectCommand(BotService botService) {
        super(botService);
    }

    @Override
    public void execute(Update update) {
        Character character = characterRepository.getById(UUID.fromString(ParsHelper.parseFromCallback(update)));
        InlineKeyboardMarkup confirmKb = InlineKeyboardUtil.confirmationButtons("CHARACTER");
        editMessage(update,
                "Ви вибрали персонажа **" + character.getName() + "**. Підтвердити вибір?",
                confirmKb);
    }

    @Override
    public void confirm(Update update) {
        editMessage(update,
                "Персонаж підтверджено! Гра починається.",
                null);
    }

    @Override
    public void undo(Update update) {
        editMessage(update,
                "Вибір персонажа скасовано. Оберіть персонажа:",
                ParsHelper.createCharactersKeyboard());
    }
}