package utadborda.application.services;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;

import java.util.List;

public interface TagService {
    List<String> getAllDistinctCategoryFromTag();

    List<Tag> getAll();
    Tag addTag(Tag tag);
    Restaurant addTags(List<Tag> tags);
    List<Tag> getAllByCategory(String category);
    List<Restaurant> getByName(String category, String name);


}
