package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import space.hideaway.UserNotFoundException;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.repositories.RoleRepository;
import space.hideaway.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserManagementImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SecurityServiceImplementation securityServiceImplementation;

    @Autowired
    public UserManagementImpl(RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                              SecurityServiceImplementation securityServiceImplementation) {
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.securityServiceImplementation = securityServiceImplementation;
    }


    /**
     * Save a new user into the database.
     *
     * @param user The new user to be saved.
     */
    @Override
    public void save(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        user.setRoleSet(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    /**
     * Obtain the user that is currently logged in.
     *
     * @return The user that is currently logged in.
     */
    public User getCurrentLoggedInUser() {
        try {
            return findByUsername(securityServiceImplementation.findLoggedInUsername());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find a user by username.
     *
     * @param username The username associated with the user to be obtained.
     * @return The located user.
     * @throws UserNotFoundException If the user is not valid, and doesn't exist in the database.
     */
    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("The user was not found in the database.");
        } else {
            return user;
        }
    }

    /**
     * Obtain a list of devices for a user.
     *
     * @param username The username associated with user to obtain devices for.
     * @return A list of devices for a given username.
     * @throws UserNotFoundException If the user is not valid, and doesn't exist in the database.
     */
    @Override
    public Set<Device> getDevices(String username) throws UserNotFoundException {
        return findByUsername(username).getDeviceSet();
    }

    @Override
    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }


}
