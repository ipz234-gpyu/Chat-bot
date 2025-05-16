package bot.domain;

import bot.domain.Interface.Identifiable;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Player implements Identifiable {
    private UUID userId;
    private String username;
    private Map<String, String> additionalInfo;

    public Player(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
        this.additionalInfo = new LinkedHashMap<>();
    }

    public Player() {}

    // Геттери та сеттери

    public UUID getId() {
        return userId;
    }

    public void setId(UUID userId) {
        this.userId = userId;
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
