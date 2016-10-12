package space.hideaway.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by dough on 10/12/2016.
 */
@Controller
public class LoginController {

    /**
     * Login is routed via intercept-url in the config files, but this code allows
     * the controller to inject attributes. In other words, validation of fields in the
     * form.
     *
     * @param model The active model injected by Spring MVC.
     * @return The name of the template to be directed to, assuming no errors.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model) {
        return "dashboard";
    }
}
