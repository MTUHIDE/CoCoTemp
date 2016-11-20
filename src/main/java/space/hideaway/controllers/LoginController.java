package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.services.LoginImpl;

/**
 * HIDE CoCoTemp 2016
 * Class responsible for routing requests relating to logging in a user.
 *
 * @author Piper Dougherty
 */
@Controller
public class LoginController {

    /**
     * The application security service responsible for handling operations
     * relating to authenticating a user.
     */
    private final LoginImpl loginImpl;

    @Autowired
    public LoginController(LoginImpl loginImpl) {
        this.loginImpl = loginImpl;
    }


    /**
     * Login a user via API. Returns a JSON representation of the success or failure of the
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
    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public
    @ResponseBody
    String login(@RequestParam("username") String username,
                 @RequestParam("password") String password) {
        return loginImpl.tryLogin(username, password);
    }
}
