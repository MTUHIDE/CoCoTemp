package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SecurityServiceImplementation implements SecurityService {


    private final
    UserDetailsServiceImplementation userDetailsServiceImplementation;


    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityServiceImplementation(AuthenticationManager authenticationManager, UserDetailsServiceImplementation userDetailsServiceImplementation) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
    }


    @Override
    public String findLoggedInUsername() {
        Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
        return userDetails.getName();
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


    @Transactional(readOnly = true)
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
