package utadborda.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TimeRangeService;
import utadborda.application.services.UserService;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {
    private RestaurantService restaurantService;
    private TimeRangeService timeRangeService;
    private UserService userService;

    @Autowired
    public DataInitializer(
            RestaurantService restaurantService,
            TimeRangeService timeRangeService,
            UserService userService
    ) {
        this.restaurantService = restaurantService;
        this.timeRangeService = timeRangeService;
        this.userService = userService;
    }

    public void run(ApplicationArguments args) {
        List<TimeRange> dominosHours = new ArrayList<TimeRange>();
        Restaurant dominos = new Restaurant("Dominos", "581-2345", "Hjarðarhagi", dominosHours);
        restaurantService.addRestaurant(dominos);
        for(int i = 0; i < 7; i++) {
            TimeRange timeRange = new TimeRange(Time.valueOf("8:00:00"), Time.valueOf("19:00:00"), i, false, null, dominos);
            timeRangeService.addTimeRange(timeRange);
            dominosHours.add(timeRange);
        }
        restaurantService.addRestaurant(new Restaurant("Pizzan", "591-2845", "Hringbraut", null));
        restaurantService.addRestaurant(new Restaurant("Black-box", "489-2345", "Hjallarstíg", null));
        restaurantService.addRestaurant(new Restaurant("Sbarro", "581-2425", "Kringlan", null));
        try {
            userService.registerNewUser(new UserDTO("test", "test", "test", "test@test.is"));
            userService.registerAdmin();
        } catch (GeneralExceptions.UserAlreadyExistsException uaeEX) {
            System.out.println("Error creating admin");
        }
    }
}
