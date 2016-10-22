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
import space.hideaway.services.SecurityServiceImplementation;
import space.hideaway.services.UserDetailsServiceImplementation;

/**
 * Created by dough on 10/12/2016.
 */
@Controller
public class LoginController {

    @Autowired
    private SecurityServiceImplementation securityServiceImplementation;

    /**
     * User login with .json response, for logging in user via AJAx.
     *
     * @return JSON response specifying the login status, true or false, and
     * if false, a description of the error.
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public
    @ResponseBody
    String login(@RequestParam("username") String username,
                 @RequestParam("password") String password) {
        return securityServiceImplementation.tryLogin(username, password);
    }
}
