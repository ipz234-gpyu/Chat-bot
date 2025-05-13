package bot.util;

import io.github.cdimascio.dotenv.Dotenv;

public class BotConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String TELEGRAM_BOT_TOKEN = dotenv.get("TELEGRAM_BOT_TOKEN");
    public static final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");
}