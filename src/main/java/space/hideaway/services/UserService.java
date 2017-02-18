package space.hideaway.services;

import space.hideaway.exceptions.UserNotFoundException;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.Set;

public interface UserService {

    /**
     * Save a new user into the database.
     *
     * @param user The new user to be saved.
     */
    void save(User user);

    void update(User user);

    /**
     * Obtain the user that is currently logged in.
     *
     * @return The user that is currently logged in.
     */
    User getCurrentLoggedInUser();

    /**
     * Find a user by username.
     * @param username The username associated with the user to be obtained.
     * @return The located user.
     * @throws UserNotFoundException If the user is not valid, and doesn't exist in the database.
     */
    User findByUsername(String username) throws UserNotFoundException;


    /**
     * Obtain a list of devices for a user.
     *
     * @param username The username associated with user to obtain devices for.
     * @return A list of devices for a given username.
     * @throws UserNotFoundException If the user is not valid, and doesn't exist in the database.
     */
    Set<Device> getDevices(String username) throws UserNotFoundException;

    User findByEmail(String email);
}
