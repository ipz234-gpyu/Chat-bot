package bot.domain;

import bot.domain.Interface.Identifiable;
import bot.infrastructure.telegram.enums.BotStateType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class UserSession implements Identifiable {
    @JsonProperty("sessionId")
    private UUID sessionId;

    @JsonProperty("chatId")
    private Long chatId;

    @JsonProperty("sessionName")
    private String sessionName;

    @JsonProperty("selectedStoryId")
    private UUID selectedStoryId;

    @JsonProperty("selectedCharacterId")
    private UUID selectedCharacterId;

    @JsonIgnore
    private Story selectedStory;

    @JsonIgnore
    private Character selectedCharacter;

    @JsonProperty("state")
    private BotStateType state;

    public UserSession(UUID sessionId, Long chatId, UUID selectedStoryId, UUID selectedCharacterId) {
        this.sessionId = sessionId;
        this.chatId = chatId;
        this.selectedStoryId = selectedStoryId;
        this.selectedCharacterId = selectedCharacterId;
    }

    public UserSession(Long chatId) {
        this.chatId = chatId;
    }

    public UserSession() { }

    @Override
    public UUID getId() {
        return sessionId;
    }

    public BotStateType getState() {
        return state;
    }

    public void setState(BotStateType state) {
        this.state = state;
    }

    @Override
    public void setId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public UUID getSelectedCharacterId() {
        return selectedCharacterId;
    }

    public void setSelectedCharacterId(UUID selectedCharacterId) {
        this.selectedCharacterId = selectedCharacterId;
    }

    public UUID getSelectedStoryId() {
        return selectedStoryId;
    }

    public void setSelectedStoryId(UUID selectedStoryId) {
        this.selectedStoryId = selectedStoryId;
    }

    public Long getChatId() {
        return chatId;
    }

    public Story getSelectedStory() {
        return selectedStory;
    }

    public void setSelectedStory(Story selectedStory) {
        this.selectedStory = selectedStory;
    }

    public Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(Character selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }

    @Override
    public String toString() {
        return "Історія: " + getSessionName();
    }
}