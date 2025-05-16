package bot.infrastructure.storage;

import bot.domain.StoryTreeNode;
import bot.infrastructure.storage.Interface.IStoryCharacterRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class StoryCharacterRepository extends JsonSessionRepository<StoryTreeNode> implements IStoryCharacterRepository {
    public StoryCharacterRepository(Long userId) {
        super(userId, "story_nps.json", StoryTreeNode.class);
    }

    @Override
    public boolean addNode(StoryTreeNode node) {
        return save(node) != null;
    }

    @Override
    public List<StoryTreeNode> getByNpcResponse(String contains) {
        return getAll().stream()
                .filter(n -> n.getNpcResponse() != null && n.getNpcResponse().contains(contains))
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