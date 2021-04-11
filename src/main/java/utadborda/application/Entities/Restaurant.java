package utadborda.application.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;
import java.util.List;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String phone;
    private String address;
    private Double posLat;
    private Double posLng;
    private String website;
    @JsonManagedReference
    @ManyToMany(mappedBy = "restaurant")
    private List<TimeRange> openingHours;
    @Column(columnDefinition = "varchar(1024)")
    @ElementCollection
    private List<String> photos;
    private String gmapsId;
    private String gmapsUrl;
    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "restaurant_tags",
        joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    )
    private List<Tag> tags;
    @OneToMany(mappedBy = "restaurant")
    private List<MenuItem> menu;
    private String cuisineType;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "UAB_User_id")
    private UAB_User owner;

    protected Restaurant() {}

    public Restaurant(
            String name,
            String phone,
            String address,
            String website,
            String photos,
            List<Tag> tags,
            List<MenuItem> menu,
            String cuisineType,
            UAB_User owner
            ) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.website = website;
        this.photos = new ArrayList<>();
        this.photos.add(photos);
        this.tags = tags;
        this.menu = menu;
        this.cuisineType = cuisineType;
        this.owner = owner;
    }

    public Restaurant(
            String name,
            String address,
            Double posLat,
            Double posLng,
            String gmapsId,
            String gmapsUrl
    ) {
        this.name = name;
        this.address = address;
        this.posLat = posLat;
        this.posLng = posLng;
        this.gmapsId = gmapsId;
        this.gmapsUrl = gmapsUrl;
        this.openingHours = new ArrayList<TimeRange>();
        this.tags = new ArrayList<Tag>();
        this.menu = new ArrayList<MenuItem>();
    }

    public Restaurant(
            String name,
            String address,
            Double posLat,
            Double posLng,
            String gmapsId,
            String gmapsUrl,
            String website,
            String phone,
            List<String> photos
    ) {
        this.name = name;
        this.address = address;
        this.posLat = posLat;
        this.posLng = posLng;
        this.gmapsId = gmapsId;
        this.gmapsUrl = gmapsUrl;
        this.openingHours = new ArrayList<TimeRange>();
        this.tags = new ArrayList<Tag>();
        this.menu = new ArrayList<MenuItem>();
        this.website = website;
        this.phone = phone;
        this.photos = new ArrayList<>();
        if(photos != null) {
            this.photos = photos;
        }
    }

    public Restaurant(
            String name,
            String address,
            Double posLat,
            Double posLng,
            String website,
            String phone,
            List<TimeRange> openingHours,
            String photos,
            String gmapsId,
            String gmapsUrl,
            List<Tag> tags,
            List<MenuItem> menu
    ) {
        this.name = name;
        this.address = address;
        this.posLat = posLat;
        this.posLng = posLng;
        this.website = website;
        this.phone = phone;
        this.openingHours = openingHours;
        this.photos = new ArrayList<>();
        this.photos.add(photos);
        this.gmapsId = gmapsId;
        this.gmapsUrl = gmapsUrl;
        this.tags = tags;
        this.menu = menu;
    }

    public UUID getId() {
        return id;
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

    public Double getPosLat() {
        return posLat;
    }

    public void setPosLat(Double posLat) {
        this.posLat = posLat;
    }

    public Double getPosLng() {
        return posLng;
    }

    public void setPosLng(Double posLng) {
        this.posLng = posLng;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public List<String> getPhotos() {
        return this.photos;
    }

    public String getPhoto(int index) {
        if (index < photos.size())
            return photos.get(index);
        return null;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void addPhoto(String photoReference) {
        this.photos.add(photoReference);
    }

    public String getGmapsId() {
        return gmapsId;
    }

    public void setGmapsId(String gmapsId) {
        this.gmapsId = gmapsId;
    }

    public String getGmapsUrl() {
        return gmapsUrl;
    }

    public void setGmapsUrl(String gmapsUrl) {
        this.gmapsUrl = gmapsUrl;
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

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addTags(List<Tag> tags) {this.tags.addAll(tags); }

    public String toString() {
        return this.getName();
    }

    public UAB_User getOwner() {
        return owner;
    }

    public void setOwner(UAB_User owner) {
        this.owner = owner;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public void addTimeRange(TimeRange timeRange) {
        this.openingHours.add(timeRange);
    }

    public int getCurrentDay() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch(day) {
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 4;
            default:
                return 5;
        }
    }

    public String getOpeningHoursToday() {
        int day = this.getCurrentDay();
        for(TimeRange time : this.getOpeningHours()) {
            if(time.getWeekDay() == day) {
                return time.hoursToString();
            }
        }
        return "Closed";
    }
}
