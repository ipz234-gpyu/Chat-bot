package bot.infrastructure.storage.Interface;

import bot.domain.StoryTreeNode;

import java.util.List;
import java.util.UUID;

public interface IStoryCharacterRepository extends IRepository<StoryTreeNode> {
    boolean addNode(StoryTreeNode node);
    List<StoryTreeNode> getByNpcResponse(String contains);
    void saveAll(List<StoryTreeNode> items, UUID sessionId);
}
