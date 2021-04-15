package utadborda.application;

import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DTO.CloudinaryResponseDTO;
import utadborda.application.services.DTO.GmapsDataDTO;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.services.TimeRangeService;
import utadborda.application.services.UserService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@ConditionalOnProperty(
    prefix = "data.init",
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true
)

@Component
public class DataInitializer implements ApplicationRunner {
    private final RestaurantService restaurantService;
    private final TimeRangeService timeRangeService;
    private final UserService userService;
    private final TagService tagService;
    private final ObjectMapper objectMapper;
    private final Cloudinary cloudinary;

    @Autowired
    public DataInitializer(
            RestaurantService restaurantService,
            TimeRangeService timeRangeService,
            UserService userService,
            TagService tagService,
            ObjectMapper objectMapper
    ) {
        this.restaurantService = restaurantService;
        this.timeRangeService = timeRangeService;
        this.userService = userService;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.cloudinary = new Cloudinary(Stream.of(
            new AbstractMap.SimpleEntry<>("cloud_name", "dwx7hyahv"),
            new AbstractMap.SimpleEntry<>("api_key", "377459241788154"),
            new AbstractMap.SimpleEntry<>("api_secret", "oBO1nzGak8XIuTpR4q0PfI98yY0")
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     *
     * Autorun for our application data initializer.
     *
     * @param args
     */
    public void run(ApplicationArguments args) throws Exception {
        /**
         * Database insert from file. Keep in while spring.jpa.hibernate.ddl-auto is == to create in application.properties.
         * Otherwise run only once and comment out to avoid multiple loads of the dataset into the db.
         */
        addToDatabase("scraper/merged_data_complete.json", -1);

        try {
            userService.registerNewUser(new UserDTO("test", "test", "test", "test@test.is", new SimpleDateFormat("dd/MM/yy").parse("04/12/97")));
            userService.registerAdmin();
        } catch (GeneralExceptions.UserAlreadyExistsException | ParseException e) {
            System.out.println("Error creating admin");
        }
    }

    private String loadJsonAsString(String filename) throws IOException {
        StringBuilder builder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)) {
            stream.forEach(s -> builder.append(s).append('\n'));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    /**
     * Data parser for google maps data that adds data from JSON file in path to database.
     * Parses data, creates objects and adds them to arraylists for later use.
     * <p>
     * Can add photos, reviews and maybe even a menu later if the data for that
     * ever becomes available. Photos and reviews are scraped but not yet addable to db.
     *
     * @param path - Path to JSON data file.
     */
    private void addToDatabase(String path, int limit) throws Exception {


        System.out.println("Adding data to database\nThis might take a while...");

        String jsonString = loadJsonAsString(path);
        GmapsDataDTO data = objectMapper.readValue(jsonString, GmapsDataDTO.class);
        data.results = data.results.subList(0,500);

        data.setImages(insertImages(data));

        System.out.println("Parsed jsonData, creating Restaurants");
        List<Restaurant> restaurants = data.getRestaurants();
        restaurants = restaurantService.addRestaurants(restaurants);
        data.updatePlaces(restaurants);

        System.out.println("Created restaurants, creating TimeRanges");
        List<TimeRange> timeRanges = data.getTimeRanges();
        timeRangeService.addTimeRanges(timeRanges);

        System.out.println("Created TimeRanges, creating Tags");
        List<List<Tag>> tags = data.getTags();
        restaurants.clear();
        for (List<Tag> tag : tags) {
            restaurants.add(tagService.addTags(tag));
        }
        restaurantService.addRestaurants(restaurants);
        System.out.println("Created Tags, finished adding to database");
    }

    public Map<String, String> insertImages(GmapsDataDTO data) throws Exception {
        Map<Object, Object> params = new HashMap<>();
        params.put("public_id", "ut-ad-borda/test");
        params.put("overwrite", false);
        params.put("tags", "ut-ad-borda");

        Map<String, Object> options = new HashMap<>();
        options.put("type", "upload");
        options.put("prefix", "ut-ad-borda/");
        options.put("max_results", 500);

        JSONArray allImages = new JSONArray();
        Api.ApiResponse cloudinaryResponse = cloudinary.api().resources(options);
        allImages.addAll((JSONArray) cloudinaryResponse.get("resources"));
        options.put("next_cursor", cloudinaryResponse.get("next_cursor"));
        while(cloudinaryResponse.get("next_cursor") != null) {
            cloudinaryResponse = cloudinary.api().resources(options);
            allImages.addAll((JSONArray) cloudinaryResponse.get("resources"));
            options.put("next_cursor", cloudinaryResponse.get("next_cursor"));
        }
        String cloudinaryJson = allImages.toString();
        cloudinaryJson = "{resources: " + cloudinaryJson + "}";
        CloudinaryResponseDTO cloudinaryData = objectMapper.readValue(cloudinaryJson, CloudinaryResponseDTO.class);
        List<String> imageKeys = cloudinaryData.getKeys();

        Map<String, File> images = data.createImageMap();
        for (String imageKey : imageKeys) {
            images.remove(imageKey);
        }
        Map<String, String> cloudinaryUrls = new HashMap<>();

        System.out.println("Uploading " + images.size() + " images to cloudinary: ");
        List<imageUploader> uploaders = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (Map.Entry<String, File> image: images.entrySet()) {
            imageUploader uploader = new imageUploader(params, image.getKey(), image.getValue());
            uploaders.add(uploader);
            Thread t = new Thread(uploader);
            threads.add(t);
        }
        System.out.println("All " + threads.size() + " threads started");
        List<Thread> running = new ArrayList<>();
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).start();
            running.add(threads.get(i));
            if(threads.size() > 10 && i % (threads.size() / 10) == 0) System.out.println((i / threads.size()) + "% complete");
            if(i % 100 == 0) {
                for (Thread t: running) t.join();
                running.clear();
            }
        }
        if (!running.isEmpty()) {
            for (Thread t: running) t.join();
        }
        System.out.println("All threads finished");
        for (imageUploader uploader: uploaders) {
            cloudinaryUrls.put(uploader.getPath().getKey(), uploader.getPath().getValue());
        }
        cloudinaryUrls.putAll(cloudinaryData.createEntryList());
        return cloudinaryUrls;
    }

    public class imageUploader implements Runnable {
        private final Map<Object, Object> params;
        private final String name;
        private final File file;
        private volatile String path;

        public imageUploader(Map<Object, Object> params, String name, File file) {
            this.params = params;
            this.name = name;
            this.file = file;
        }

        @Override
        public void run() {
            if (!file.exists()) {
                this.path = "";
            } else {
                params.replace("public_id", "ut-ad-borda/" + name);
                try {
                    Map<?, ?> result = cloudinary.uploader().upload(file, params);
                    this.path = (String) result.get("secure_url");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public Map.Entry<String, String> getPath() { return new AbstractMap.SimpleEntry<>(this.name, this.path); }
    }
}