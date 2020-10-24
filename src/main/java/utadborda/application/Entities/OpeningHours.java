package utadborda.application.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "openingHours")
    private List<TimeRange> openingHours;

    protected OpeningHours() {}

    public OpeningHours(List<TimeRange> openingHours) {
        this.openingHours = openingHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TimeRange> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<TimeRange> openingHours) {
        this.openingHours = openingHours;
    }

    public void addOpeningHours(TimeRange openingHour) { this.openingHours.add(openingHour); }
}
