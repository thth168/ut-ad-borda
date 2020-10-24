package utadborda.application.Entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
public class TimeRange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Time openTime;
    private Time closeTime;
    private int weekDay;
    private boolean holiday;
    private Date specialDate;

    @ManyToOne
    private OpeningHours openingHours;

    protected TimeRange() {}

    public TimeRange(
            Time openTime,
            Time closeTime,
            int weekDay,
            boolean holiday,
            Date specialDate,
            OpeningHours openingHours
    ) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.weekDay = weekDay;
        this.holiday = holiday;
        this.specialDate = specialDate;
        this.openingHours = openingHours;
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

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }
}
