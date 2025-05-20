package bot.infrastructure.openai.models.Interface;

import bot.domain.Character;

public interface ICharacterModel {
    Character generateCharacter(String prompt);
}
