package bot.domain;

import java.util.List;

public class Story {
    private String id;
    private String title;
    private String description;
    private String prompt;
    private List<String> tags;
    //private StoryTree storyTree;

    public Story() {
    }

    public Story(String id, String title, String description, String prompt, List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.prompt = prompt;
        this.tags = tags;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prompt='" + prompt + '\'' +
                ", tags=" + tags +
                '}';
    }
}