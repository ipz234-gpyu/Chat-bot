package bot.infrastructure.storage.Interface;

import bot.domain.StoryTreeNode;

import java.util.List;
import java.util.UUID;

public interface IStoryTreeRepository extends IRepository<StoryTreeNode> {
    boolean addNode(StoryTreeNode node);
    List<StoryTreeNode> findByStage(int stage);
    void saveAll(List<StoryTreeNode> items, UUID sessionId);
}
