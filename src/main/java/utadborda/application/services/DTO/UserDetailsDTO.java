package utadborda.application.services.DTO;

import org.springframework.format.annotation.DateTimeFormat;
import utadborda.application.Annotations.PasswordMatches;
import utadborda.application.Annotations.ValidEmail;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserDetailsDTO{

	private String email;
	private String username;
	private List<Restaurant> listRestaurants;

	public UserDetailsDTO(User user){
		this.email = user.getEmail();
		this.username = user.getUserName();
		this.listRestaurants = user.getRestaurants();
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public List<Restaurant> getRestaurants() {
		return listRestaurants;
	}

}
