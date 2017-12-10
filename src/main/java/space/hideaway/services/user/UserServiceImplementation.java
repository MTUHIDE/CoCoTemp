package space.hideaway.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import space.hideaway.exceptions.UserNotFoundException;
import space.hideaway.model.Role;
import space.hideaway.model.site.Site;
import space.hideaway.model.User;
import space.hideaway.repositories.RoleRepository;
import space.hideaway.repositories.UserRepository;
import space.hideaway.services.security.SecurityServiceImplementation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserServiceImplementation implements UserService
{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SecurityServiceImplementation securityServiceImplementation;

    @Autowired
    public UserServiceImplementation(
            RoleRepository roleRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserRepository userRepository,
            SecurityServiceImplementation securityServiceImplementation)
    {
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
    public void save(User user)
    {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_PUBLIC");
        user.setRoleSet(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    /**
     * Updates a user's information.
     *
     * @param user the user to update
     */
    @Override
    public void update(User user)
    {
        User oldUser = userRepository.getOne(user.getId());

        if (oldUser != null)
        {
            if (user.getEmail() == null)
                user.setEmail(oldUser.getEmail());
            if (user.getFirstName() == null)
                user.setFirstName(oldUser.getFirstName());
            if (user.getMiddleInitial() == null)
                user.setMiddleInitial(oldUser.getMiddleInitial());
            if (user.getLastName() == null)
                user.setLastName(oldUser.getLastName());
            if (user.getUsername() == null)
                user.setUsername(oldUser.getUsername());
            if (user.getPassword() == null)
                user.setPassword(oldUser.getPassword());
            if (user.getRoleSet() == null)
                user.setRoleSet(oldUser.getRoleSet());
            if (user.getSiteSet() == null)
                user.setSiteSet(oldUser.getSiteSet());
            if (user.getDeviceSet() == null)
                user.setDeviceSet(oldUser.getDeviceSet());
            if (user.getUploadHistorySet() == null)
                user.setUploadHistorySet(oldUser.getUploadHistorySet());
        }

        userRepository.save(user);
    }

    /**
     * Obtain the user that is currently logged in.
     *
     * @return The user that is currently logged in.
     */
    public User getCurrentLoggedInUser()
    {
        try
        {
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
    public User findByUsername(String username) throws UserNotFoundException
    {
        User user = userRepository.findByUsername(username);
        if (user == null)
        {
            throw new UserNotFoundException("The user was not found in the database.");
        } else {
            return user;
        }
    }

    /**
     * Obtain a list of sites for a user.
     *
     * @param username The username associated with user to obtain sites for.
     * @return A list of sites for a given username.
     * @throws UserNotFoundException If the user is not valid, and doesn't exist in the database.
     */
    @Override
    public Set<Site> getSites(String username) throws UserNotFoundException
    {
        return findByUsername(username).getSiteSet();
    }

    /**
     * Finds a user based on their email.
     *
     * @param email The email associated with the user
     * @return the user with the associated email
     */
    @Override
    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }


}
