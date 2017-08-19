package space.hideaway.exceptions;

/**
 * Handles the case when a user is not found in the database.
 */
public class UserNotFoundException extends Throwable {
    /**
     * User not found.
     *
     * @param s Information about the exception.
     */
    public UserNotFoundException(String s) {
        super(s);
    }
}
