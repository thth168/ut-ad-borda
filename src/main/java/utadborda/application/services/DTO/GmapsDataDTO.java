package utadborda.application.services.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.TimeRange;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GmapsDataDTO {
    public static Map<String, String> allowedTypes = Stream.of(
            new AbstractMap.SimpleEntry<>("food", "food"),
            new AbstractMap.SimpleEntry<>("restaurant", "restaurant"),
            new AbstractMap.SimpleEntry<>("night_club", "nightClub"),
            new AbstractMap.SimpleEntry<>("bakery", "bakery"),
            new AbstractMap.SimpleEntry<>("cafe", "cafe"),
            new AbstractMap.SimpleEntry<>("bar", "bar"),
            new AbstractMap.SimpleEntry<>("gas_station", "gasStation"),
            new AbstractMap.SimpleEntry<>("liquor_store", "liquorStore"),
            new AbstractMap.SimpleEntry<>("convenience_store", "grocer"),
            new AbstractMap.SimpleEntry<>("grocery_or_supermarket", "grocer"),
            new AbstractMap.SimpleEntry<>("supermarket", "grocer")
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    public List<Places> results;

    @JsonIgnore
    public List<Restaurant> getRestaurants() {
        List<Restaurant> allRestaurants = new ArrayList<>();
        List<Places> toRemove = new ArrayList<>();
        for (Places place: results) {
            if (place.tags != null)
                allRestaurants.add(place.createRestaurant());
            else toRemove.add(place);
        }
        for (Places place: toRemove) {
            results.remove(place);
        }
        return allRestaurants;
    }

    @JsonIgnore
    public void updatePlaces(List<Restaurant> restaurants) {
        for (int i = 0; i < results.size(); i++) {
            Places place = results.get(i);
            place.restaurant = restaurants.get(i);
            results.set(i, place);
        }
    }

    @JsonIgnore
    public List<TimeRange> getTimeRanges() {
        List<TimeRange> timeRanges = new ArrayList<>();
        for (Places place: results) {
            timeRanges.addAll(place.createTimeRanges());
        }
        return timeRanges;
    }

    @JsonIgnore
    public List<List<Tag>> getTags() {
        List<List<Tag>> tags = new ArrayList<>();
        for (Places place: results) {
            tags.add(place.createTags());
        }
        return tags;
    }

    @JsonIgnore
    public Map<String, File> createImageMap() {
        Map<String, File> imageMap = new HashMap<>();
        int j = 0;
        for (Places place: results) {
            if (place.photos != null) {
                for (int i = 0; i < 2 && i < place.photos.size(); i++) {
                    String filename = Math.abs(place.name.hashCode()) + "_" + j++ + "-" + i;
                    imageMap.put(filename, new File("src/main/resources/static/r/" + place.photos.get(i) + ".png"));
                    place.photos.set(i, filename);
                }
            }
        }
        return imageMap;
    }

    @JsonIgnore
    public void setImages(Map<String, String> imageMappings) {
        for (Places place: results) {
            if (place.photos != null) {
                for (int i = 0; i < 2 && i < place.photos.size(); i++) {
                    place.photos.set(i, imageMappings.get(place.photos.get(i)));
                }
                if(place.photos.size() > 2)
                    place.photos = place.photos.subList(0, 2);
            }
        }
    }

    public static class Places {
        public String name;
        public String formatted_address;
        public double lat;
        public double lng;
        public String place_id;
        public String url;
        public String website;
        public String international_phone_number;
        public List<AbstractMap.SimpleEntry<String, String>> tags;
        public Opening_hours opening_hours;
        public List<String> photos;
        @JsonIgnore
        public Restaurant restaurant;

        @JsonSetter("geometry")
        public void setGeometry(Geometry geometry) {
            lat = geometry.location.lat;
            lng = geometry.location.lng;
        }

        @JsonSetter("url")
        public void setUrl(String url) {
            if (url.length() < 255) this.url = url;
        }

        @JsonSetter("website")
        public void setWebsite(String url) {
            if (url.length() < 255) this.website = url;
        }

        @JsonSetter("types")
        public void setTypes(List<String> types) {
            for (String type: types) {
                String value = allowedTypes.getOrDefault(type, null);
                if (value != null) {
                    if (this.tags == null) this.tags = new ArrayList<>();
                    this.tags.add(new AbstractMap.SimpleEntry<>("type", value));
                }
            }
        }

        @JsonSetter("address_components")
        public void setAddressTags(List<Address_component> addressComponents) {
            if (this.tags == null) return;
            for (Address_component addressComponent: addressComponents) {
                if (addressComponent.long_name == null) continue;
                List<String> categories = Arrays.asList("", "district", "city", "postalCode");
                int value = 0;
                for (String s: addressComponent.types) {
                    switch (s) {
                        case "postal_code":
                        case "subpremise":
                            value = 3;
                            break;
                        case "locality":
                            value = Math.max(value, 2);
                            break;
                        case "sublocality":
                        case "sublocality_level_1":
                            value = Math.max(value, 1);
                            break;
                    }
                }
                if (value != 0) {
                    this.tags.add(new AbstractMap.SimpleEntry<>(categories.get(value), addressComponent.long_name));
                }
            }
        }

        @JsonSetter("photos")
        public void setPhotos(List<Photo> photos) {
            this.photos = new ArrayList<>();
            for (Photo photo: photos) {
                this.photos.add(photo.photo_reference);
            }
        }

        @JsonIgnore
        public Restaurant createRestaurant() {
            return new Restaurant(
                    name, formatted_address,
                    lat, lng, place_id, url,
                    website, international_phone_number,
                    photos
            );
        }

        @JsonIgnore
        public List<TimeRange> createTimeRanges() {
            List<TimeRange> timeRanges = new ArrayList<>();
            if (opening_hours != null) {
                for (Period openingHours: opening_hours.periods) {
                    timeRanges.add(new TimeRange(openingHours.open, openingHours.close, openingHours.day, restaurant));
                }
            }
            return timeRanges;
        }

        @JsonIgnore
        public List<Tag> createTags() {
            List<Tag> tags = new ArrayList<>();
            for (AbstractMap.SimpleEntry<String, String> tag: this.tags) {
                Tag newTag = new Tag(tag.getValue(), tag.getKey());
                newTag.addRestaurant(restaurant);
                tags.add(newTag);
            }
            return tags;
        }
    }

    public static class Geometry {
        public Location location;
    }

    public static class Location {
        public double lat;
        public double lng;
    }

    public static class Address_component {
        public List<String> types;
        public String long_name;
    }

    public static class Opening_hours {
        public List<Period> periods;
        @JsonSetter("periods")
        public void setPeriods(List<Period> periods) {
            if (periods.size() == 1 &&
                periods.get(0).close == null &&
                periods.get(0).day == 0
            ) {
                this.periods = new ArrayList<>();
                Period tfh = new Period();
                tfh.open = "00:00:00";
                tfh.close = "24:00:00";
                for (int i = 0; i < 7; i++) {
                    tfh.day = i;
                    this.periods.add(tfh);
                }
            } else {
                this.periods = periods;
            }
        }
    }

    public static class Period {
        public int day;
        public String open;
        public String close;
        public static SimpleDateFormat inFormat = new SimpleDateFormat("HHmm");
        public static SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
        @JsonSetter("open")
        public void setOpen(OCTime open) throws ParseException {
            this.open = outFormat.format(inFormat.parse(open.time));
            this.day = open.day;
        }
        @JsonSetter("close")
        public void setClose(OCTime close) throws ParseException {
            this.close = outFormat.format(inFormat.parse(close.time));
        }
    }

    public static class OCTime {
        public int day;
        public String time;
    }

    public static class Photo {
        public String photo_reference;
    }
}
