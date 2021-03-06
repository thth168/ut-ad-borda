package utadborda.application.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
public class TimeRange {
    @Id
    @GeneratedValue
    private UUID id;
    private String openTime;
    private String closeTime;
    private int weekDay;
    private boolean holiday;
    private String specialDate;
    @JsonBackReference
    @ManyToMany
    @JoinColumn(name = "restaurant_id")
    private List<Restaurant> restaurant;

    protected TimeRange() {}

    public TimeRange(
            String openTime,
            String closeTime,
            int weekDay,
            boolean holiday,
            String specialDate,
            Restaurant restaurant
    ) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weekDay = weekDay;
        this.holiday = holiday;
        this.specialDate = specialDate;
        this.restaurant = new ArrayList<>();
        this.restaurant.add(restaurant);
    }

    public TimeRange(
            String openTime,
            String closeTime,
            int weekDay,
            boolean holiday,
            String specialDate
    ) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weekDay = weekDay;
        this.holiday = holiday;
        this.specialDate = specialDate;
        this.restaurant = new ArrayList<>();
    }

    public TimeRange(
            String openTime,
            String closeTime,
            int weekDay,
            Restaurant restaurant
    ) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weekDay = weekDay;
        this.holiday = false;
        this.specialDate = null;
        this.restaurant = new ArrayList<>();
        this.restaurant.add(restaurant);
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public String getSpecialDate() {
        return specialDate;
    }

    public void setSpecialDate(String specialDate) {
        this.specialDate = specialDate;
    }

    public UUID getId() {
        return id;
    }

    public List<Restaurant> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }

    public void addRestaurant(Restaurant restaurant) {
        this.restaurant.add(restaurant);
    }

    public String weekdayToString(int day) {
        switch (day) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            default:
                return "Sunday";
        }
    }

    public String hoursToString() {
        if (this.getOpenTime().toString().equals("00:00:00") && this.getOpenTime().toString().equals("00:00:00")) {
            return "Open 24 hours";
        }
        String str = "";
        if(this.getOpenTime() == null) return str;
        str += this.getOpenTime().toString().replaceFirst(":00", "");
        str += " - ";
        String cl = this.getCloseTime().toString().replaceFirst(":00", "");
        if (cl.equals("00:00")) {
            str += "24:00";
        } else {
            str += cl;
        }
        return str;
    }

    public String toString() {
        return weekdayToString(this.getWeekDay()) + ": " + hoursToString();
    }

}
