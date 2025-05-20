package bot.domain;

import bot.domain.Interface.Identifiable;

import java.util.List;
import java.util.UUID;

public class CharacterTreeNode implements Identifiable {
    private UUID id;
    private UUID parentId;
    private String userResponse;
    private String npcResponse;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public String getNpcResponse() {
        return npcResponse;
    }

    public void setNpcResponse(String npcResponse) {
        this.npcResponse = npcResponse;
    }
}
