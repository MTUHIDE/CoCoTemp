package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import space.hideaway.UserValidator;
import space.hideaway.model.User;
import space.hideaway.services.SecurityServiceImplementation;
import space.hideaway.services.UserService;

/**
 * cocotemp
 *
 * @author Piper Dougherty
 * @since 10/19/2016
 */
@Controller
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityServiceImplementation securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model)
    {
        String username = userForm.getUsername();
        String password = userForm.getPassword();

        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "index";
        }
        userService.save(userForm);
        securityService.autoLogin(username, password);
        return "redirect:/dashboard";
    }
}
