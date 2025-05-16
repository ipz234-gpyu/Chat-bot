package bot.infrastructure.storage;

import bot.domain.Player;
import bot.infrastructure.storage.Interface.IPlayerRepository;

import java.util.UUID;

public class PlayerRepository extends JsonSessionRepository<Player> implements IPlayerRepository {

    public PlayerRepository(Long userId) {
        super(userId, "player.json", Player.class);
    }

    public boolean updateAdditionalInfo(UUID sessionId, String key, String value) {
        Player player = getById(sessionId);
        if (player == null) return false;

        player.getAdditionalInfo().put(key, value);
        save(player);
        return true;
    }
}
