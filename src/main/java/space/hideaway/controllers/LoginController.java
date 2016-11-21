package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.services.SecurityServiceImplementation;


@Controller
public class LoginController {


    private final SecurityServiceImplementation securityServiceImplementation;

    @Autowired
    public LoginController(SecurityServiceImplementation securityServiceImplementation) {
        this.securityServiceImplementation = securityServiceImplementation;
    }


    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public
    @ResponseBody
    String login(@RequestParam("username") String username,
                 @RequestParam("password") String password) {
        return securityServiceImplementation.tryLogin(username, password);
    }
}
