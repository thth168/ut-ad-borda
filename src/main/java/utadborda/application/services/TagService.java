package utadborda.application.services;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAll();
    Tag addTag(Tag tag);
    List<Tag> getAllByCategory(String category);

}
