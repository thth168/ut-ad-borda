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
    List<Restaurant> findAllByIdNotNull(Pageable pageable);
    long countAllByTagsContaining(Tag tag);

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