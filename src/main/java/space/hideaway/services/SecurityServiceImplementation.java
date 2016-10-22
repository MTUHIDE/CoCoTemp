package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by dough on 10/9/2016.
 */
@Service
public class SecurityServiceImplementation implements SecurityService {

    @Autowired
    private
    UserDetailsServiceImplementation userDetailsServiceImplementation;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }
        return null;
    }

    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );


        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    public String tryLogin(String username, String password) {
        UserDetails userDetails = null;

        try {
            userDetails = userDetailsServiceImplementation.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            //TODO use GSON library.
            return "{\"status\": false, \"error\": \"Username or password is incorrect.\"}";
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            return "{\"status\": true, " +
                    "\"location\": \"/dashboard\"}";
        }
        return "{\"status\": false, \"error\": \"Username or password is incorrect.\"}";
    }
}
