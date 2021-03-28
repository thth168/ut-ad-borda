package utadborda.application.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
public class Match {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    private Restaurant restaurant;
    private int agree;

    protected Match() {}

    public Match(
        Restaurant restaurant,
        int agree
    ) {
        this.restaurant = restaurant;
        this.agree = agree;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }
}
