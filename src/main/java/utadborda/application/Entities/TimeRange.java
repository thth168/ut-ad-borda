package utadborda.application.Entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.UUID;

@Entity
public class TimeRange {
    @Id
    @GeneratedValue
    private UUID id;
    private Time openTime;
    private Time closeTime;
    private int weekDay;
    private boolean holiday;
    private Date specialDate;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    protected TimeRange() {}

    public TimeRange(
            Time openTime,
            Time closeTime,
            int weekDay,
            boolean holiday,
            Date specialDate,
            Restaurant restaurant
    ) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weekDay = weekDay;
        this.holiday = holiday;
        this.specialDate = specialDate;
        this.restaurant = restaurant;
    }

    public TimeRange(
            Time openTime,
            Time closeTime,
            int weekDay,
            boolean holiday,
            Date specialDate
    ) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weekDay = weekDay;
        this.holiday = holiday;
        this.specialDate = specialDate;
    }

    public TimeRange(
            Time openTime,
            Time closeTime,
            int weekDay,
            Restaurant restaurant
    ) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weekDay = weekDay;
        this.holiday = false;
        this.specialDate = null;
        this.restaurant = restaurant;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    public Time getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Time closeTime) {
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

    public Date getSpecialDate() {
        return specialDate;
    }

    public void setSpecialDate(Date specialDate) {
        this.specialDate = specialDate;
    }

    public UUID getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String weekdayToString() {
        switch (this.getWeekDay()) {
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
        return weekdayToString() + ": " + hoursToString();
    }

}
