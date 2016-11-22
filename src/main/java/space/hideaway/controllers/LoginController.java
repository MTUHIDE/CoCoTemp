package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.services.SecurityService;


/**
 * The controller responsible for logging in via API call.
 */
@Controller
public class LoginController {

    /**
     * The service responsible for logging in a user.
     */
    private final SecurityService securityService;

    @Autowired
    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Log in a user via API call.
     * <p>
     * URL: /login.json
     * Secured: No
     * Method: POST
     * <p>
     * Sample Successful
     * {
     * "status": true,
     * "location": "/dashboard"
     * }
     * <p>
     * Sample Failed
     * {
     * "status": false,
     * "error": "Username or password is incorrect."
     * }
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A json representation of the status.
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public
    @ResponseBody
    String login(@RequestParam("username") String username,
                 @RequestParam("password") String password) {
        return securityService.tryLogin(username, password);
    }
}
