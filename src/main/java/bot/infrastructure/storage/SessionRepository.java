package bot.infrastructure.storage;

import bot.domain.UserSession;
import bot.infrastructure.storage.Interface.ISessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SessionRepository implements ISessionRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path USER_DIR;

    public SessionRepository(Long userId) {
        USER_DIR = Paths.get("data", "users", userId.toString());

        if (!Files.exists(USER_DIR)) {
            try {
                Files.createDirectories(USER_DIR);
            } catch (IOException e) {
                throw new RuntimeException("Не вдалося створити директорію для користувача: " + USER_DIR, e);
            }
        }
    }

    protected File[] getSessionDirs (){
        File userDir = USER_DIR.toFile();
        if (!userDir.exists()) return new File[0];

        File[] sessionDirs = userDir.listFiles(File::isDirectory);
        return sessionDirs != null ? sessionDirs : new File[0];
    }

    @Override
    public List<UserSession> getAll() {
        List<UserSession> sessions = new ArrayList<>();
        File[] sessionDirs = getSessionDirs();

        for (File sessionDir : sessionDirs) {
            File sessionFile = new File(sessionDir, "session.json");
            if (sessionFile.exists()) {
                try {
                    UserSession session = objectMapper.readValue(sessionFile, UserSession.class);
                    sessions.add(session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sessions;
    }

    @Override
    public UserSession save(UserSession session) {
        if (session.getId() == null)
            session.setId(UUID.randomUUID());

        Path sessionDir = USER_DIR.resolve(session.getId().toString());

        try {
            Files.createDirectories(sessionDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File sessionFile = sessionDir.resolve("session.json").toFile();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(sessionFile, session);
            return session;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserSession getById(UUID id) {
        Path sessionPath = USER_DIR.resolve(id.toString()).resolve("session.json");
        File sessionFile = sessionPath.toFile();

        if (sessionFile.exists()) {
            try {
                return objectMapper.readValue(sessionFile, UserSession.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean remove(UUID id) {
        Path sessionDir = USER_DIR.resolve(id.toString());

        try {
            if (!Files.exists(sessionDir)) return false;

            Files.walk(sessionDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
