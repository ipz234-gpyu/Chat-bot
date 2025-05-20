package bot.infrastructure.storage.Interface;

import bot.domain.Stage;
import bot.domain.StoryTreeNode;

import java.util.List;
import java.util.UUID;

public interface IStoryTreeRepository extends IRepository<StoryTreeNode> {
    boolean addNode(StoryTreeNode node, UUID sessionId);
    StoryTreeNode getLast(UUID sessionId);
    List<StoryTreeNode> findByStage(Stage stage);
    void saveAll(List<StoryTreeNode> items, UUID sessionId);
}
