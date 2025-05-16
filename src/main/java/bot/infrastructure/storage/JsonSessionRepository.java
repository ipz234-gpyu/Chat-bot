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
        List<T> items = new ArrayList<>();

        try (DirectoryStream<Path> dirs = Files.newDirectoryStream(baseDir)) {
            for (Path dir : dirs) {
                Path jsonFile = dir.resolve(fileName);
                if (Files.exists(jsonFile)) {
                    try {
                        T item = objectMapper.readValue(jsonFile.toFile(), type);
                        items.add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
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
