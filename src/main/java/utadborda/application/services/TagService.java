package utadborda.application.services;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;

import java.util.List;
import java.util.UUID;

public interface TagService {
    List<String> getAllDistinctCategoryFromTag();

    List<Tag> getAll();
    List<Tag> getAll(int page, int limit);
    Tag addTag(Tag tag);
    Tag updateTag(Tag tag);
    Restaurant addTags(List<Tag> tags);
    List<Tag> getAllByCategory(String category);
    List<Tag> getAllByCategory(String category, int page, int limit);
    List<Restaurant> getByName(String category, String name);
    Tag getTagById(UUID id);
}
