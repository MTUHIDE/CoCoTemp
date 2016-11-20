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

/**
 * HIDE CoCoTemp 2016
 * A class responsible for CRUD operations on user objects.
 *
 * @author Piper Dougherty
 */
@Service
public class UserManagementImpl implements UserManagement {

    /**
     * The service responsible for CRUD operations on user accounts.
     */
    private final UserRepository userRepository;

    /**
     * The repository responsible for obtaining and creating user roles.
     */
    private final RoleRepository roleRepository;

    /**
     * The bCrypt encoding service.
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final LoginImpl loginImpl;

    @Autowired
    public UserManagementImpl(RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                              LoginImpl loginImpl) {
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.loginImpl = loginImpl;
    }

    /**
     * Save a new user into the database. Make sure user has been validated first.
     *
     * @param user The user to be saved.
     */
    @Override
    public void save(User user) {
        //Encrypt the user's password for insertion into the database.
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        //Add specific roles to the user. Right now, all roles are added.
        user.setRoleSet(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    /**
     * Obtain a user from the database by matching the username.
     *
     * @param username The username of the user to search for.
     * @return The user with the specified username.
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

    public User getCurrentLoggedInUser() {
        try {
            return findByUsername(loginImpl.findLoggedInUsername());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all the devices for this user.
     *
     * @param username The user to obtain devices for.
     * @return A set of devices the user maintains.
     */
    @Override
    public Set<Device> getDevices(String username) throws UserNotFoundException {
        return findByUsername(username).getDeviceSet();
    }
}
