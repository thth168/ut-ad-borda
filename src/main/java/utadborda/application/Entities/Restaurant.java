package utadborda.application.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String phone;
    private String address;
    @OneToMany(mappedBy = "restaurant")
    private List<TimeRange> openingHours;
    @ManyToMany
    @JoinTable
    private List<Tag> tags;
    @OneToMany(mappedBy = "restaurant")
    private List<MenuItem> menu;
    private String cuisineType;

    protected Restaurant() {}

    public Restaurant(
            String name,
            String phone,
            String address,
            List<TimeRange> openingHours
    ){
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.openingHours = openingHours;
        this.cuisineType = "";
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

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

}
