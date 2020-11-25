package utadborda.application.Entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
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
        String str = "";
        switch (this.getWeekDay()) {
            case 0:
                str += "Monday";
                break;
            case 1:
                str += "Tuesday";
                break;
            case 2:
                str += "Wednesday";
                break;
            case 3:
                str += "Thursday";
                break;
            case 4:
                str += "Friday";
                break;
            case 5:
                str += "Saturday";
                break;
            case 6:
                str += "Sunday";
                break;
        }
        return str;
    }

    public String toString() {
        return weekdayToString() + ": " + this.getOpenTime().toString() + " - " + this.getCloseTime().toString();
    }

}
