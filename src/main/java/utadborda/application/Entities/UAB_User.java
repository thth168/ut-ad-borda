package utadborda.application.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class UAB_User {
    /** =======================
     *  Spring security queries
     *  ======================= */
    @Transient
    public final static String SPRING_SECURITY_USERNAME_QUERY =
            "SELECT email, password, true" +
                    " FROM user" +
                    " WHERE email=?";

    @Transient
    public final static String SPRING_SECURITY_AUTHORITIES_BY_USERNAME_QUERY =
            "SELECT email, roles"
                    + " FROM user JOIN user_roles ON user.id = user_roles.user_id"
                    + " WHERE email=?";

    @Id
    @GeneratedValue
    private UUID id;
    @NotNull
    @NotEmpty
    private String userName;
    private String firstName;
    private String lastName;
    @NotNull
    @NotEmpty
    private String password;
    @NotEmpty
    @NotNull
    private String email;
    private Date dateOfBirth;
    @ElementCollection
    private List<String> roles;

    @JsonManagedReference
    @OneToMany(mappedBy = "owner")
    private List<Restaurant> restaurants;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    protected UAB_User() {}

    public UAB_User(String userName, String password, String email, Date dateOfBirth, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @ElementCollection
    public List<String> getUserRoles() {
        return roles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.roles = userRoles;
    }

    public void addUserRole(String userRole) {
        this.roles.add(userRole);
    }
}
