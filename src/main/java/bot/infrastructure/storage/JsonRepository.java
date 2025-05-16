package bot.infrastructure.storage;

import bot.domain.Interface.Identifiable;
import bot.infrastructure.storage.Interface.IRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class JsonRepository<T extends Identifiable> implements IRepository<T> {
    private final File directory;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> type;

    public JsonRepository(String directoryPath, Class<T> type) {
        this.directory = new File(directoryPath);
        this.type = type;

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public synchronized T save(T item) {
        UUID id = item.getId();
        if (id == null) {
            id = UUID.randomUUID();
            item.setId(id);
        }
        File file = new File(directory, id + ".json");
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, item);
            return item;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public synchronized T getById(UUID id) {
        File file = new File(directory, id + ".json");
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public synchronized List<T> getAll() {
        List<T> result = new ArrayList<>();
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    T item = objectMapper.readValue(file, type);
                    result.add(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public synchronized boolean remove(UUID id) {
        File file = new File(directory, id + ".json");
        return file.exists() && file.delete();
    }
}