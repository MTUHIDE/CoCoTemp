package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.services.UserDetailsServiceImplementation;

/**
 * Created by dough on 10/12/2016.
 */
@Controller
public class LoginController {

    @Autowired
    UserDetailsServiceImplementation userDetailsServiceImplementation;
    @Autowired
    private
    AuthenticationManager authenticationManager;
    @Autowired
    private
    SecurityContextRepository securityContextRepository;

    /**
     * Login is routed via intercept-url in the config files, but this code allows
     * the controller to inject attributes. In other words, validation of fields in the
     * form.
     *
     * @return The name of the template to be directed to, assuming no errors.
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST)

    public
    @ResponseBody
    String login(@RequestParam("username") String username,
                 @RequestParam("password") String password) {

        UserDetails userDetails = null;

        //TODO refactor into SecurityServiceImplementation and autowire.

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
