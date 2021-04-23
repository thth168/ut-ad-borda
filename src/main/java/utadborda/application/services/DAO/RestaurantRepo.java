package utadborda.application.services.DAO;

import org.jruby.runtime.Signature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepo extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findTop20ByIdNotNull();
    Restaurant findByName(String name);
    Restaurant findByid(UUID restaurant_ID);

    @Query("SELECT r from Restaurant r, IN (r.tags) t where t in (:tags)")
    List<Restaurant> findAllByTagsIsContaining(@Param("tags") List<Tag> tag, Pageable pageable);
    @Query("select distinct r from Restaurant r where :numberOfTags = (select count(distinct tag.name) from Restaurant r2 inner join r2.tags tag where r2.id = r.id and tag IN (:tags)) ")
    List<Restaurant> findAllByTagsIsContainingAll(@Param("tags") List<Tag> tag, @Param("numberOfTags") long numTags, Pageable pageable);
    List<Restaurant> findAllByIdNotNull(Pageable pageable);
    long countAllByTagsContaining(Tag tag);
    @Query("SELECT count(r) from Restaurant r, IN (r.tags) t where t in (:tags)")
    long countAllByTagsContainingAll(@Param("tags") List<Tag> tag);
    @Query("select count(r) from Restaurant r where :numberOfTags = (select count(distinct tag.name) from Restaurant r2 inner join r2.tags tag where r2.id = r.id and tag IN (:tags)) ")
    long countAllByTagsContainingAllExclude(@Param("tags") List<Tag> tag, @Param("numberOfTags") long numTags);

    @Query(value = "select " +
            "id, address, cuisine_type, gmaps_id, gmaps_url, name, phone, pos_lat, pos_lng, website, uab_user_id " +
            "from (" +
            "select *, ( 6371 * acos( cos( radians(:lat)) * " +
            "cos( radians( pos_lat )) * cos( radians(pos_lng) - radians(:lng)) " +
            "+ sin(radians(:lat)) * sin( radians(pos_lat)))) " +
            "AS distance from restaurant) as rd where distance < :dist order by rd.distance",
            nativeQuery = true,
            countQuery = "select count(*) from restaurant"
    )
    List<Restaurant> findAllByGPS(@Param("lat") double lat, @Param("lng") double lng, @Param("dist") double distance, Pageable pageable);
}