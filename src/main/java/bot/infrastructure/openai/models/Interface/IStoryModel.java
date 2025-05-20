package bot.infrastructure.openai.models.Interface;

import bot.domain.Story;

public interface IStoryModel {
    Story generateStory(String prompt);
}
