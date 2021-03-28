package utadborda.application.services;

import utadborda.application.Entities.UAB_User;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.DTO.UserDetailsDTO;

public interface UserService {
    UAB_User registerNewUser(UserDTO user) throws GeneralExceptions.UserAlreadyExistsException;
    void registerAdmin() throws GeneralExceptions.UserAlreadyExistsException;
    UAB_User findUser(String email);

    UserDetailsDTO getUserDetails(String email);
}
