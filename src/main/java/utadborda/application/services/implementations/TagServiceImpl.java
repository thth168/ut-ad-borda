package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DAO.RestaurantRepo;
import utadborda.application.services.DAO.TagRepo;
import utadborda.application.services.TagService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TagServiceImpl implements TagService {
    private TagRepo tagRepo;
    private RestaurantRepo restaurantRepo;
    @Autowired
    public TagServiceImpl(
            TagRepo tagRepo,
            RestaurantRepo restaurantRepo
    ) {
        this.tagRepo = tagRepo;
        this.restaurantRepo = restaurantRepo;
    }

    @Override
    public Tag addTag(Tag tag) {
        return tagRepo.save(tag);
    }

    @Transactional
    @Override
    public Restaurant addTags(List<Tag> tags) {
        Restaurant restaurant = tags.get(0).getRestaurants().get(0);
        for (int i = 0; i < tags.size(); i++) {
            Tag found = tagRepo.findByCategoryAndName(tags.get(i).getCategory(), tags.get(i).getName());
            if (found != null) {
                found.addRestaurant(restaurant);
                tags.set(i, found);
            }
        }
        tags = tagRepo.saveAll(tags);
        restaurant.addTags(tags);
        return restaurant;
    }

    @Override
    public List<Tag> getAllByCategory(String category) {
        return tagRepo.findAllByCategory(category);
    }


    @Override
    public List<String> getAllDistinctCategoryFromTag() {
        return tagRepo.findAllDistinctCategoryFromTag();
    }


    @Override
    public List<Tag> getAll() {
        return tagRepo.findAll();
    }

    @Override
    public List<Restaurant> getByName(String category, String name) {
        Tag tag = tagRepo.findByCategoryAndName(category, name);
        if (tag != null)
            return tag.getRestaurants().subList(0,20);
        return null;
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepo.findById(id).orElse(null);
    }
}
