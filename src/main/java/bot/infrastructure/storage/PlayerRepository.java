package bot.infrastructure.storage;

import bot.domain.Player;
import bot.infrastructure.storage.Interface.IRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRepository implements IRepository<Player> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path DIR;

    public PlayerRepository(Long userId) {
        DIR = Paths.get("data", "users", userId.toString());

        if (!Files.exists(DIR)) {
            try {
                Files.createDirectories(DIR);
            } catch (IOException e) {
                throw new RuntimeException("Не вдалося створити директорію для користувача: " + DIR, e);
            }
        }
    }

    @Override
    public Player save(Player player) {
        if (player.getId() == null)
            player.setId(UUID.randomUUID());

        Path sessionDir = DIR.resolve(player.getId().toString());

        try {
            Files.createDirectories(sessionDir);
            File playerFile = sessionDir.resolve("player.json").toFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(playerFile, player);
            return player;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Player getById(UUID sessionId) {
        Path sessionFile = DIR.resolve(sessionId.toString()).resolve("player.json");

        if (Files.exists(sessionFile)) {
            try {
                return objectMapper.readValue(sessionFile.toFile(), Player.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<Player> getAll() {
        List<Player> players = new ArrayList<>();

        try (DirectoryStream<Path> sessionDirs = Files.newDirectoryStream(DIR)) {
            for (Path sessionDir : sessionDirs) {
                Path playerFile = sessionDir.resolve("player.json");
                if (Files.exists(playerFile)) {
                    try {
                        Player player = objectMapper.readValue(playerFile.toFile(), Player.class);
                        players.add(player);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return players;
    }

    @Override
    public boolean remove(UUID sessionId) {
        Path sessionDir = DIR.resolve(sessionId.toString());
        Path playerFile = sessionDir.resolve("player.json");

        try {
            return Files.deleteIfExists(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
