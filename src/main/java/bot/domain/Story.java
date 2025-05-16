package bot.domain;

import bot.domain.Interface.Identifiable;

import java.util.List;
import java.util.UUID;

public class Story implements Identifiable {
    private UUID id;
    private String title;
    private String description;
    private String prompt;
    private String finalStory;
    private List<String> tags;
    //private StoryTree storyTree;

    public Story() {
    }

    public Story(UUID id, String title, String description, String prompt, List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.prompt = prompt;
        this.tags = tags;
    }

    // Getters & Setters
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getFinalStory() {
        return finalStory;
    }

    public void setFinalStory(String finalStory) {
        this.finalStory = finalStory;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}