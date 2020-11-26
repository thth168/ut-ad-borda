package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DAO.TagRepo;
import utadborda.application.services.TagService;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    public TagRepo tagRepo;
    @Autowired
    public TagServiceImpl(
            TagRepo tagRepo
    ) {
        this.tagRepo = tagRepo;
    }

    @Override
    public Tag addTag(Tag tag) {
        return tagRepo.save(tag);
    }

    @Override
    public List<Tag> getAllByCategory(String category) {
        return tagRepo.findAllByCategory(category);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepo.findAll();
    }

    @Override
    public List<Restaurant> getByName(String category, String name) {
        return tagRepo.findByCategoryAndName(category, name).getRestaurants().subList(0,20);
    }
}
