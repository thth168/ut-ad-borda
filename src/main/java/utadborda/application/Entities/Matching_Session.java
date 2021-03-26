package utadborda.application.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Matching_Session {
    @Id
    @GeneratedValue
    private UUID id;
    @JsonManagedReference
    @OneToMany
    private List<UAB_User> users;
    @JsonManagedReference
    @OneToMany
    private List<Match> matches;

    protected Matching_Session() {}

    public Matching_Session(
        UAB_User user
    ) {
        users = new ArrayList<UAB_User>();
        users.add(user);
        this.matches = new ArrayList<Match>();
    }

    public List<UAB_User> getUsers() {
        return users;
    }

    public void setUsers(List<UAB_User> users) {
        this.users = users;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
