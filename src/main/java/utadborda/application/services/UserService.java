package utadborda.application.services;

import utadborda.application.Entities.User;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.DTO.UserDetailsDTO;

public interface UserService {
    User registerNewUser(UserDTO user) throws GeneralExceptions.UserAlreadyExistsException;
    void registerAdmin() throws GeneralExceptions.UserAlreadyExistsException;
    User findUser(String email);

    UserDetailsDTO getUserDetails(String email);
}
