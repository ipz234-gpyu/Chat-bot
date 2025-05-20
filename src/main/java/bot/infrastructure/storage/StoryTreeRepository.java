package bot.infrastructure.storage;

import bot.domain.Stage;
import bot.domain.StoryTreeNode;
import bot.infrastructure.storage.Interface.IStoryTreeRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class StoryTreeRepository extends JsonSessionRepository<StoryTreeNode> implements IStoryTreeRepository {
    public StoryTreeRepository(Long userId) {
        super(userId, "story_tree.json", StoryTreeNode.class);
    }

    @Override
    public boolean addNode(StoryTreeNode node, UUID sessionId) {
        return save(node, sessionId) != null;
    }

    @Override
    public StoryTreeNode getLast(UUID sessionId){
        List<StoryTreeNode> nodes = getAll(sessionId);
        return nodes.isEmpty() ? null : nodes.get(nodes.size() - 1);
    }

    @Override
    public List<StoryTreeNode> findByStage(Stage stage) {
        return getAll().stream()
                .filter(n -> n.getStage() == stage)
                .toList();
    }

    @Override
    public void saveAll(List<StoryTreeNode> items, UUID sessionId) {
        try {
            Path sessionDir = baseDir.resolve(sessionId.toString());
            Files.createDirectories(sessionDir);
            File file = sessionDir.resolve(fileName).toFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
