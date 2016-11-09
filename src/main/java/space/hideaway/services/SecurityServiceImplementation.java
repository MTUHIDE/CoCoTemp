package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * HIDE CoCoTemp 2016
 * <p>
 * The class responsible for handling operations
 * relating to authenticating a user.
 *
 * @author Piper Dougherty
 */
@Service
public class SecurityServiceImplementation implements SecurityService {

    /**
     * The service responsible for obtaining a user from the database by username.
     */
    private final
    UserDetailsServiceImplementation userDetailsServiceImplementation;

    /**
     * The service responsible for authenticating users.
     */
    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityServiceImplementation(AuthenticationManager authenticationManager, UserDetailsServiceImplementation userDetailsServiceImplementation) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
    }

    /**
     * Obtain the currently logged in user from the Spring security context.
     * <p>
     * TODO: if this method returns null if nobody is logged in, it may break things. Fix it.
     *
     * @return The username of the currently logged in user, or null if there is no logged in user.
     */
    @Override
    public String findLoggedInUsername() {
        Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
        return userDetails.getName();
    }

    /**
     * Automatically login user after registration.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(username);

        //Create a new token for the user.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );


        try {
            //Try authenticating the user.
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            //Tell Spring security the user is valid, add the user to the session.
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    /**
     * Login a user. Returns a JSON representation of the success or failure of the
     * login.
     * <p>
     * Sample of JSON structure of successful login.
     * {
     * "status": true,
     * "location": "/dashboard"
     * }
     * <p>
     * Sample of JSON structure of unsuccessful login.
     * {
     * "status": false,
     * "error": "The form is invalid for some reason."
     * }
     *
     * @param username The username of the user to be logged in.
     * @param password The password of the user to be logged in.
     * @return JSON structure representing the status of the login.
     */
    public String tryLogin(String username, String password) {
        try {
            UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    password,
                    userDetails.getAuthorities()
            );
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                return "{\"status\": true, " +
                        "\"location\": \"/dashboard\"}";
            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        } catch (AuthenticationException e) {
            return "{\"status\": false, \"error\": \"Username or password is incorrect.\"}";
        }

        return "{\"status\": false, \"error\": \"Username or password is incorrect.\"}";
    }
}
