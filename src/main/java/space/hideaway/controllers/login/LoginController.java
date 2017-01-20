package space.hideaway.controllers.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.services.SecurityService;


/**
 * The controller responsible for logging in via API call.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * The service responsible for logging in a user.
     */
    private final SecurityService securityService;

    @Autowired
    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping
    public String showLogin()
    {
        return "/login/login";
    }

    @RequestMapping(params = "_error")
    public String showLoginError(Model model)
    {
        model.addAttribute("loginError", true);
        return "/login/login";
    }
}
