package bot.infrastructure.storage.Interface;

import java.util.List;
import java.util.UUID;

public interface IRepository<T> {
    T save(T item);
    T getById(UUID id);
    List<T> getAll();
    boolean remove(UUID id);
}