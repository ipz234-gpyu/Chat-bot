package bot.infrastructure.storage;

import bot.domain.Story;

public class StoryRepository extends JsonRepository<Story> {
    private static final String STORIES_DIR = "data/stories/";

    public StoryRepository() {
        super(STORIES_DIR, Story.class);
    }
}