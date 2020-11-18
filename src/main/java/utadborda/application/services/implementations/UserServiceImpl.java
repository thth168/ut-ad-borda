package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.User;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DAO.UserRepo;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.DTO.UserDetailsDTO;
import utadborda.application.services.UserService;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User registerNewUser(UserDTO user) throws GeneralExceptions.UserAlreadyExistsException {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new GeneralExceptions.UserAlreadyExistsException("There is an account with that email address: " + user.getEmail());
        }
        return userRepo.save(new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                user.getDateOfBirth(),
                Arrays.asList("USER")
        ));
    }

    @Transactional
    @Override
    public void registerAdmin() throws GeneralExceptions.UserAlreadyExistsException {
        userRepo.save(new User(
            "admin",
            passwordEncoder.encode("admin"),
            "admin@admin.is",
            null,
            Arrays.asList("USER", "ADMIN")
        ));
    }
    @Transactional
    @Override
    public UserDetailsDTO getUserDetails(String email) {
        UserDetailsDTO userdetails = new UserDetailsDTO(userRepo.findByEmail(email));
        return userdetails;
    }
}
