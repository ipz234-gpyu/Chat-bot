package bot.domain;

import bot.domain.Interface.Identifiable;

import java.util.List;
import java.util.UUID;

public class StoryTreeNode implements Identifiable {
    private UUID id;
    private String userResponse;
    private String npcResponse;
    private String action;
    private List<String> options;
    private int stage;

    public StoryTreeNode(UUID id, String userResponse, String npcResponse, String action, List<String> options, int stage) {
        this.id = id;
        this.userResponse = userResponse;
        this.npcResponse = npcResponse;
        this.action = action;
        this.options = options;
        this.stage = stage;
    }

    public StoryTreeNode() {}

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNpcResponse() {
        return npcResponse;
    }

    public void setNpcResponse(String npcResponse) {
        this.npcResponse = npcResponse;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
