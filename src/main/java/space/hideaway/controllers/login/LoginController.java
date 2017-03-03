package space.hideaway.controllers.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Serve the login page to the user.
 */
@Controller
@RequestMapping("/login")
public class LoginController
{

    /**
     * The default mapping, returns the login page.
     * @return The path to the login template.
     */
    @RequestMapping
    public String showLogin()
    {
        return "login/login";
    }

    /**
     * The mapping for a login error. Sprint automatically appends a ?_error
     * parameter when the login fails. We add an "error flag" to turn on the error
     * message for the login template.
     *
     * @param model The Spring model for the login page.
     * @return THe path to the login template.
     */
    @RequestMapping(params = "_error")
    public String showLoginError(Model model)
    {
        model.addAttribute("loginError", true);
        return "login/login";
    }
}
