package utadborda.application.services.DTO;

import org.springframework.format.annotation.DateTimeFormat;
import utadborda.application.Annotations.PasswordMatches;
import utadborda.application.Annotations.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@PasswordMatches
public class UserDTO {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateOfBirth;

    public UserDTO(
            @NotNull @NotEmpty String username,
            @NotNull @NotEmpty String password,
            String matchingPassword,
            @NotNull @NotEmpty String email,
            @NotNull @NotEmpty Date dateOfBirth
    ) {
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public UserDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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
}
