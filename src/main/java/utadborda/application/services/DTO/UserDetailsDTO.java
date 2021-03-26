package utadborda.application.services.DTO;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.UAB_User;

import java.util.Date;
import java.util.List;

public class UserDetailsDTO{

	private String email;
	private String username;
	private Date dateOfBirth;
	private List<Restaurant> listRestaurants;

	public UserDetailsDTO(UAB_User UABUser){
		this.email = UABUser.getEmail();
		this.username = UABUser.getUserName();
		this.dateOfBirth = UABUser.getDateOfBirth();;
		this.listRestaurants = UABUser.getRestaurants();
	}

	public Date getDateOfBirth() { return dateOfBirth; }

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
