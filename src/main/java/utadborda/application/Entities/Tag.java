package utadborda.application.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.jruby.RubyBoolean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Tag {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String category;
    @JsonBackReference
    @ManyToMany(mappedBy = "tags")
    private List<Restaurant> restaurants;

    public Tag() {}

    public Tag(String name, String category, List<Restaurant> restaurants) {
        this.name = name;
        this.category = category;
        if(restaurants == null) {
            this.restaurants = new ArrayList<Restaurant>();
        } else {
            this.restaurants = restaurants;
        }
    }

    public Tag(String name, String category) {
        this.name = name;
        this.category = category;
        this.restaurants = new ArrayList<Restaurant>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getFancyName() {
        switch (name){
            case "bakery":
                return "Bakery";
            case "grocer":
                return "Grocery Store";
            case "cafe":
                return "Café";
            case "restaurant":
                return "Restaurant";
            case "liquorStore":
                return "Liquor Store";
            case "bar":
                return "Pub";
            case "nightClub":
                return "Night Club";
            case "gasStation":
                return "Fast Food";
            default:
                return name;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
    }

    public String toString() {
        return this.getCategory() + ": " + this.getName();
    }

}
