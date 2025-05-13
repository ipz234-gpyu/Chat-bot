package bot.infrastructure.storage;

import bot.infrastructure.storage.Interface.IRepository;

public class CharacterRepository extends JsonRepository<Character> {
    private static final String CHARACTERS_DIR = "data/characters/";

    public CharacterRepository() {
        super("CHARACTERS_DIR", Character.class);
    }
}