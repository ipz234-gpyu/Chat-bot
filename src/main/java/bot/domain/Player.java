package bot.domain;

import bot.domain.Interface.Identifiable;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Player implements Identifiable {
    private UUID playerId;
    private String username;
    private Map<String, String> additionalInfo;

    public Player(String username) {
        this.username = username;
        this.additionalInfo = new LinkedHashMap<>();
    }

    public Player() {}

    // Геттери та сеттери

    public UUID getId() {
        return playerId;
    }

    public void setId(UUID userId) {
        this.playerId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
