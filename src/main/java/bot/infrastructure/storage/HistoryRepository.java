package bot.infrastructure.storage;

import bot.domain.HistoryEntry;
import bot.domain.StoryTreeNode;
import bot.infrastructure.storage.Interface.IHistoryRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class HistoryRepository extends JsonSessionRepository<HistoryEntry> implements IHistoryRepository {
    public HistoryRepository(Long userId) {
        super(userId, "history.json", HistoryEntry.class);
    }

    @Override
    public boolean appendEntry(String sender, String message) {
        HistoryEntry entry = new HistoryEntry();
        entry.setId(UUID.randomUUID());
        entry.setSender(sender);
        entry.setMessage(message);
        entry.setTimestamp(System.currentTimeMillis());

        return save(entry) != null;
    }

    @Override
    public List<HistoryEntry> getLastN(int n) {
        List<HistoryEntry> all = getAll();
        return all.subList(Math.max(all.size() - n, 0), all.size());
    }

    @Override
    public void saveAll(List<HistoryEntry> items, UUID sessionId) {
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
