package bot.domain;

import bot.infrastructure.enums.BotStateType;
import bot.infrastructure.state.*;
import bot.domain.Character;
import bot.domain.Story;

public class UserSession {
    private final Long chatId;
    private BotStateType state;
    private Story selectedStory;
    private Character selectedCharacter;

    public UserSession(Long chatId) {
        this.chatId = chatId;
        this.state = BotStateType.INIT_SESSION;
    }

    public void setState(BotStateType state) {
        this.state = state;
    }

    public BotStateType getState() {
        return state;
    }

    public void setSelectedStory(Story story) {
        this.selectedStory = story;
    }

    public Story getSelectedStory() {
        return selectedStory;
    }

    public void setSelectedCharacter(Character character) {
        this.selectedCharacter = character;
    }

    public Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public Long getChatId() {
        return chatId;
    }
}