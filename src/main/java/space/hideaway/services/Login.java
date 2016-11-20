package space.hideaway.services;

public interface Login {

    /**
     * Obtain the username of the currently logged in user.
     */
    String findLoggedInUsername();

    /**
     * Automatically login user after registration.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    void autoLogin(String username, String password);
}
