package utadborda.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import utadborda.application.Entities.MenuItem;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.services.TimeRangeService;
import utadborda.application.services.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

@ConditionalOnProperty(
    prefix = "data.init",
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true
)

@Component
public class DataInitializer implements ApplicationRunner {
    private RestaurantService restaurantService;
    private TimeRangeService timeRangeService;
    private UserService userService;
    private TagService tagService;

    @Autowired
    public DataInitializer(
            RestaurantService restaurantService,
            TimeRangeService timeRangeService,
            UserService userService,
            TagService tagService
    ) {
        this.restaurantService = restaurantService;
        this.timeRangeService = timeRangeService;
        this.userService = userService;
        this.tagService = tagService;
    }

    /**
     * Autorun for our application data initializer.
     *
     * @param args
     */
    public void run(ApplicationArguments args) {
        /**
         * Database insert from file. Keep in while spring.jpa.hibernate.ddl-auto is == to create in application.properties.
         * Otherwise run only once and comment out to avoid multiple loads of the dataset into the db.
         */
        addToDatabase("scraper/merged_data_complete.json");

        try {
            userService.registerNewUser(new UserDTO("test", "test", "test", "test@test.is", new SimpleDateFormat("dd/MM/yy").parse("04/12/97")));
            userService.registerAdmin();
        } catch (GeneralExceptions.UserAlreadyExistsException | ParseException e) {
            System.out.println("Error creating admin");
        }
    }

    /**
     * File loader for json file with path file.
     *
     * @param file path to the file
     * @return JSONObject of the file given.
     */
    private JSONObject loadJSON(String file) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Data parser for google maps data that adds data from JSON file in path to database.
     * Parses data, creates objects and adds them to arraylists for later use.
     *
     * Can add photos, reviews and maybe even a menu later if the data for that
     * ever becomes available. Photos and reviews are scraped but not yet addedable to db.
     *
     * @param path - Path to JSON data file.
     */
    private void addToDatabase(String path) {
        JSONObject file = loadJSON(path);
        JSONArray data = (JSONArray) file.get("results");

        int restaurantCount = 0;
        int timeRangeCount = 0;
        int tagCount = 0;
        ArrayList<Tag> tags = new ArrayList<Tag>();
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

        ArrayList<String> allowedTypes = new ArrayList<String>(Arrays.asList(
                "food",
                "restaurant",
                "night_club",
                "bakery",
                "cafe",
                "bar",
                "gas_station",
                "liquor_store",
                "convenience_store",
                "grocery_or_supermarket",
                "supermarket"
        ));

        ArrayList<String> photoList = new ArrayList<String>(Arrays.asList((new File("src/main/resources/static/r")).list()));

        for (JSONObject place : (Iterable<JSONObject>) data) {
            try {
                String name;
                String address;
                Double posLat;
                Double posLng;
                String gmapsId;
                String gmapsUrl;

                List<TimeRange> openingHours;
                name = (String) place.get("name");
                address = (String) place.get("formatted_address");

                JSONObject location = (JSONObject) ((JSONObject) place.get("geometry")).get("location");
                posLat = (Double) location.get("lat");
                posLng = (Double) location.get("lng");
                gmapsId = (String) place.get("place_id");
                gmapsUrl = (String) place.get("url");

                Restaurant restaurant = new Restaurant(name, address, posLat, posLng, gmapsId, gmapsUrl);

                restaurant.setWebsite(place.containsKey("website") ? (((String) place.get("website")).length() > 255 ? null : ((String) place.get("website"))) : null);
                restaurant.setPhone(place.containsKey("international_phone_number") ? (String) place.get("international_phone_number") : null);

                if (place.containsKey("types")) {
                    ArrayList<String> types = (ArrayList<String>)((JSONArray)place.get("types"));

                    if (!Collections.disjoint(types, allowedTypes)) {

                        restaurant = restaurantService.addRestaurant(restaurant);
                        restaurantCount ++;

                        for (String type : types) {
                            hasType: {
                                String tagName;

                                switch (type) {
                                    case "restaurant":
                                        tagName = "restaurant";
                                        break;
                                    case "night_club":
                                        tagName = "nightClub";
                                        break;
                                    case "bakery":
                                        tagName = "bakery";
                                        break;
                                    case "cafe":
                                        tagName = "cafe";
                                        break;
                                    case "bar":
                                        tagName = "bar";
                                        break;
                                    case "gas_station":
                                        tagName = "gasStation";
                                        break;
                                    case "liquor_store":
                                        tagName = "liquorStore";
                                        break;
                                    case "convenience_store":
                                    case "grocery_or_supermarket":
                                    case "supermarket":
                                        tagName = "grocer";
                                        break;
                                    default:
                                        break hasType;
                                }

                                String tagCategory = "type";
                                if (!filterTags(tags, restaurant, tagCategory, tagName, tagCount)) {
                                    tagCount ++;
                                }

                            }

                        }

                    } else continue;

                } else continue;

                if (place.containsKey("address_components")) {
                    ArrayList<JSONObject> addrComps = (ArrayList<JSONObject>) ((JSONArray) place.get("address_components"));

                    for (JSONObject addrComp : addrComps) {
                        ArrayList<String> types = (ArrayList<String>) addrComp.get("types");

                        hasType: {
                            String tagCategory;

                            if (types.contains("postal_code") || types.contains("subpremise")) {
                                tagCategory = "postalCode";
                            } else if (types.contains("locality")) {
                                tagCategory = "city";
                            } else if (types.contains("sublocality") || types.contains("sublocality_level_1")) {
                                tagCategory = "district";
                            } else {
                                break hasType;
                            }

                            String tagName = (String) addrComp.get("long_name");
                            if (!filterTags(tags, restaurant, tagCategory, tagName, tagCount)) {
                                tagCount ++;
                            }

                        }

                    }

                }

                if (place.containsKey("opening_hours")) {
                    JSONObject opening_hours = (JSONObject) place.get("opening_hours");

                    if (opening_hours.containsKey("periods")) {
                        ArrayList<String> weekday = (ArrayList<String>) ((JSONArray) opening_hours.get("weekday_text"));
                        String[] weekdays = weekday.toArray(new String[weekday.size()]);
                        SimpleDateFormat time_12 = new SimpleDateFormat("hh:mm a");
                        SimpleDateFormat time_24 = new SimpleDateFormat("HH:mm:ss");

                        for (int i = 0; i < weekdays.length; i++) {
                            String[] sp = weekdays[i].split(": ", 2);

                            switch (sp[1]) {
                                case "Closed":
                                    break;

                                case "Open 24 hours":
                                    timeRangeService.addTimeRange(
                                            new TimeRange(
                                                    Time.valueOf("00:00:00"),
                                                    Time.valueOf("24:00:00"),
                                                    i,
                                                    restaurant
                                            )
                                    );
                                    timeRangeCount ++;
                                    break;

                                default:
                                    String[] times = sp[1].split(" – |, ");
                                    String str = "";

                                    for (int j = 0; j < times.length; j += 2) {
                                        String[] open = times[j].split("( )");

                                        if (open.length == 2) {
                                            String open_M = open[1];
                                        } else {
                                            times[j] += " " + times[j + 1].split("( )")[1];
                                        }

                                        timeRangeService.addTimeRange(
                                                new TimeRange(
                                                        Time.valueOf(time_24.format(time_12.parse(times[j]))),
                                                        Time.valueOf(time_24.format(time_12.parse(times[j + 1]))),
                                                        i,
                                                        restaurant
                                                )
                                        );
                                        timeRangeCount ++;

                                    }
                                    break;
                            }

                        }

                    }

                }

                if (place.containsKey("photos")) {
                    JSONArray photoObject = (JSONArray) place.get("photos");

                    for (Object p : photoObject) {
                        JSONObject photo = (JSONObject) p;

                        String reference = (String) photo.get("photo_reference") + ".png";

                        if (photoList.contains(reference)) {
                            restaurant.addPhoto("r/" + reference);
                        }

                    }

                }

                restaurants.add(restaurant);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Data insertion into databse was unsuccessful.");
            }

        }

        try {
            for (Tag tag : tags) {
                tagService.addTag(tag);
            }

            for (Restaurant restaurant : restaurants) {
                restaurantService.updateRestaurant(restaurant);
            }

            System.out.println("Added " + timeRangeCount + " timeRanges to database.");
            System.out.println("Added " + tagCount + " tags to database.");
            System.out.println("Added " + restaurantCount + " restaurants to database.");
            System.out.println("Data insertion into databse was successful.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data insertion into databse was unsuccessful.");
        }

    }

    /**
     * Filter helper function for parseData.
     * replacement for duplicate code.
     *
     * @param tags
     * @param restaurant
     * @param tagCategory
     * @param tagName
     */
    private boolean filterTags(ArrayList<Tag> tags, Restaurant restaurant, String tagCategory, String tagName, int tagCount) {
        boolean tagExists = false;

        for (Tag tag : tags) {
            if (tag.getName().equals(tagName) && tag.getCategory().equals(tagCategory)) {
                tag.addRestaurant(restaurant);
                restaurant.addTag(tag);
                tagExists = true;
                break;
            }

        }

        if (!tagExists) {
            Tag newTag = new Tag(tagName, tagCategory);
            newTag.addRestaurant(restaurant);
            restaurant.addTag(newTag);
            tags.add(newTag);
        }

        return tagExists;

    }

}
