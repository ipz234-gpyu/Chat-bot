package bot.infrastructure.storage.Interface;

import bot.domain.Interface.Identifiable;

import java.util.List;
import java.util.UUID;

public interface IRepository<T extends Identifiable> {
    T save(T item);
    T getById(UUID id);
    List<T> getAll();
    boolean remove(UUID id);
}