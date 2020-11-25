package utadborda.application.Entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import utadborda.application.Entities.User;

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
    @OneToMany(mappedBy = "restaurant")
    private List<TimeRange> openingHours;
    @Column(columnDefinition = "varchar(1024)")
    private String photos;
    private String gmapsId;
    private String gmapsUrl;
    @ManyToMany
    @JoinTable
    private List<Tag> tags;
    @OneToMany(mappedBy = "restaurant")
    private List<MenuItem> menu;
    private String cuisineType;

    protected Restaurant() {}

    public Restaurant( String name, String address ) {
        this.name = name;
        this.address = address;
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
        this.cuisineType = "";
        this.photos = photos;
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
        if (this.photos == null) {
            return new ArrayList<String>();
        }
        return new ArrayList<String>(Arrays.asList(this.photos.split("&")));
    }

    public String getPhoto(int index) {
        if (this.photos == null) {
            return "";
        }
        String[] str = this.photos.split("&");
        if (str.length <= index) {
            return str[str.length-1];
        } else {
            return str[index];
        }
    }

    public void setPhotos(List<String> photos) {
        this.photos = String.join("&", photos);
    }

    public void addPhoto(String photoReference) {
        if (this.photos == null) {
            this.photos = photoReference;
        } else {
            this.photos += "&" + photoReference;
        }
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

    public String toString() {
        return this.getName();
    }

}
