package space.hideaway.services.user;

import space.hideaway.exceptions.UserNotFoundException;
import space.hideaway.model.site.Site;
import space.hideaway.model.User;

import java.util.Set;

public interface UserService {

    /**
     * Save a new user into the database.
     *
     * @param user The new user to be saved.
     */
    void save(User user);

    /**
     * Updates a user's information.
     *
     * @param user the user to update
     */
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
     * Obtain a list of sites for a user.
     *
     * @param username The username associated with user to obtain sites for.
     * @return A list of sites for a given username.
     * @throws UserNotFoundException If the user is not valid, and doesn't exist in the database.
     */
    Set<Site> getSites(String username) throws UserNotFoundException;

    /**
     * Finds a user base on their email.
     *
     * @param email The email associated with the user
     * @return the user with the associated email
     */
    User findByEmail(String email);
}
