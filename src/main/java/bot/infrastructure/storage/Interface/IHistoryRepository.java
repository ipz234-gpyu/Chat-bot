package bot.infrastructure.storage.Interface;

import bot.domain.HistoryEntry;

import java.util.List;
import java.util.UUID;

public interface IHistoryRepository extends IRepository<HistoryEntry> {
    boolean appendEntry(String sender, String message);
    List<HistoryEntry> getLastN(int n);
    void saveAll(List<HistoryEntry> items, UUID sessionId);
}
