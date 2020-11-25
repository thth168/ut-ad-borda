package utadborda.application.services.DTO;

import org.springframework.util.AutoPopulatingList;
import utadborda.application.Entities.*;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantDTO {

    private UUID id;
    private String name;
    private String address;
    private String phone;
    private String cuisineType;
    private List<TimeRange> openingHours;
    private List<Tag> tags;
    private List<MenuItem> menu;
    private String website;
    private String photos;

    public RestaurantDTO(Restaurant restaurant){
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.phone = restaurant.getPhone();
        this.cuisineType = restaurant.getCuisineType();
        this.openingHours = restaurant.getOpeningHours();
        this.tags = restaurant.getTags();
        this.menu = restaurant.getMenu();
        this.photos = restaurant.getPhoto(0);
        this.website = restaurant.getWebsite();
    }

    public RestaurantDTO(){
        this.name = "";
        this.address = "";
        this.phone = "";
        this.cuisineType = "";
        this.openingHours = new ArrayList<TimeRange>();
        this.tags = new ArrayList<Tag>();
        this.menu = new ArrayList<MenuItem>();
        this.website = "";
        this.photos = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<TimeRange> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<TimeRange> openingHours) {
        this.openingHours = openingHours;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(int index) {
        this.tags.remove(index);
    }

    public void addTimeRange(TimeRange timeRange) {
        this.openingHours.add(timeRange);
    }

    public void removeTimeRange(int index) {
        this.openingHours.remove(index);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Restaurant convertToRestaurant(User user) {
        Restaurant restaurant = new Restaurant(
                this.name,
                this.phone,
                this.address,
                this.website,
                this.photos,
                this.tags,
                this.menu,
                this.cuisineType,
                user
        );
        return restaurant;
    }
}
