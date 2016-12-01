package space.hideaway.services;

import space.hideaway.UserNotFoundException;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.Set;

public interface UserService {
    /**
     * Save a user into the database.
     *
     * @param user The user to be saved.
     */
    void save(User user);

    /**
     * Obtain a user from a given username.
     *
     * @param username The username of the user to search for.
     * @return The user corresponding to the username provided.
     */
    User findByUsername(String username) throws UserNotFoundException;

    /**
     * Get the set of devices for a user.
     *
     * @param username The user to obtain devices for.
     * @return A set of devices the user maintains.
     */
    Set<Device> getDevices(String username) throws UserNotFoundException;
}
