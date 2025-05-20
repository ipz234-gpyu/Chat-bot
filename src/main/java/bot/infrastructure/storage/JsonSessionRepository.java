package bot.infrastructure.storage;

import bot.domain.Interface.Identifiable;
import bot.infrastructure.storage.Interface.IRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class JsonSessionRepository<T extends Identifiable> implements IRepository<T> {
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final Path baseDir;
    protected final String fileName;
    private final Class<T> type;

    public JsonSessionRepository(Long userId, String fileName, Class<T> type) {
        this.baseDir = Paths.get("data", "users", userId.toString());
        this.fileName = fileName;
        this.type = type;

        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалося створити базову директорію: " + baseDir, e);
        }
    }

    public T save(T item, UUID sessionId) {
        if (item.getId() == null)
            item.setId(UUID.randomUUID());
        Path itemDir = baseDir.resolve(sessionId.toString());
        Path jsonFilePath = itemDir.resolve(fileName);

        try {
            Files.createDirectories(itemDir);
            List<T> items = new ArrayList<>();
            if (Files.exists(jsonFilePath)) {
                items = objectMapper.readValue(
                        jsonFilePath.toFile(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, type)
                );
            }

            items.add(item);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFilePath.toFile(), items);
            return item;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T save(T item) {
        if (item.getId() == null)
            item.setId(UUID.randomUUID());

        Path itemDir = baseDir.resolve(item.getId().toString());

        try {
            Files.createDirectories(itemDir);
            File jsonFile = itemDir.resolve(fileName).toFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, item);
            return item;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T getById(UUID id) {
        Path filePath = baseDir.resolve(id.toString()).resolve(fileName);
        if (Files.exists(filePath)) {
            try {
                return objectMapper.readValue(filePath.toFile(), type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        Path jsonFile = baseDir.resolve(fileName);

        if (Files.exists(jsonFile)) {
            try {
                return objectMapper.readValue(
                        jsonFile.toFile(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, type)
                );
            } catch (IOException e) {
                throw new RuntimeException("Не вдалося прочитати список із файлу " + jsonFile, e);
            }
        }
        return Collections.emptyList();
    }
    public List<T> getAll(UUID sessionId) {
        Path jsonFile = baseDir.resolve(sessionId.toString()).resolve(fileName);

        if (Files.exists(jsonFile)) {
            try {
                return objectMapper.readValue(
                        jsonFile.toFile(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, type)
                );
            } catch (IOException e) {
                throw new RuntimeException("Не вдалося прочитати список із файлу " + jsonFile, e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean remove(UUID id) {
        Path filePath = baseDir.resolve(id.toString()).resolve(fileName);
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
