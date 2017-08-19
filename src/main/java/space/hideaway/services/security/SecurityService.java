package space.hideaway.services.security;

public interface SecurityService {
    /**
     * Obtain the username of the current logged in user.
     *
     * @return The username of the current logged in user.
     */
    String findLoggedInUsername();

    /**
     * Attempt to log in a user server-side.
     *
     * @param username The username of the user to log in.
     * @param password The password of the user to log in.
     * @return JSON response representing
     */
    String tryLogin(String username, String password);

    /**
     * Attempt to log in a user after they have registered.
     *
     * @param username The username of the user to log in.
     * @param password The password of the user to log in.
     */
    void autoLogin(String username, String password);
}
