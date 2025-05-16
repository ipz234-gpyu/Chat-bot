package bot.infrastructure.storage.Interface;

import bot.domain.Player;

import java.util.UUID;

public interface IPlayerRepository extends IRepository<Player> {
    boolean updateAdditionalInfo(UUID sessionId, String key, String value);
}
