package bot.domain;

import bot.domain.Interface.Identifiable;

import java.util.Map;
import java.util.UUID;

public class Character implements Identifiable {
    private UUID id;
    private String name;
    private String description;
    private String prompt;
    //private Map<String, Object> memory;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String toString() { return getName(); }
}