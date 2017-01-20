package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import space.hideaway.UserValidator;
import space.hideaway.model.User;
import space.hideaway.services.SecurityService;
import space.hideaway.services.UserService;


@Controller
@RequestMapping("/register")
@SessionAttributes("user")
public class RegisterController {

    private final UserService userService;
    private final SecurityService securityService;
    private final UserValidator userValidator;

    @Autowired
    public RegisterController(SecurityService securityService, UserValidator userValidator, UserService userService) {
        this.securityService = securityService;
        this.userValidator = userValidator;
        this.userService = userService;
    }


    @RequestMapping
    public String initialPage(final ModelMap modelMap)
    {
        modelMap.addAttribute("user", new User());
        return "/registration/register";
    }

    @RequestMapping(params = "_question", method = RequestMethod.POST)
    public String questionPage(
            final @ModelAttribute("user") User user,
            final Errors errors)
    {
        return "/registration/questionPage";
    }

    @RequestMapping(params = "_finish")
    public String processFinish(
            final @ModelAttribute("user") User user,
            final BindingResult bindingResult,
            final ModelMap modelMap,
            final SessionStatus sessionStatus)
    {

        String username = user.getUsername();
        String password = user.getPassword();

        //Validate the form.
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {

            return "register";
        }

        //At this point, all the validation has passed. Save the new account and login the user.
        userService.save(user);
        securityService.autoLogin(username, password);

        sessionStatus.setComplete();
        return "redirect:dashboard";
    }

    @RequestMapping(params = "_cancel")
    public String processCancel(
            final SessionStatus sessionStatus
    )
    {
        sessionStatus.setComplete();
        return "index";
    }


    /**
     * The endpoint for the registration page.
     * <p>
     * URL: /register
     * Secured: No
     * Method: GET
     *
     * @param model The model maintained by Spring.
     * @return The name of the registration page template.
     */
//    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    public String loadRegistration(Model model) {
//
//        //Inject a new user into the form to be filled out and returned.
//        model.addAttribute("user", new User());
//
//        //Refers to the register.html file.
//        return "register";
//    }

    /**
     * The endpoint for registration form submitting.
     *
     * @param userForm      The user object injected when the page was loaded for the first time by the user.
     * @param bindingResult Model for injecting errors.
     * @param model         The model maintained by Spring.
     * @return The name of the template to be returned. Reloads the registration page if errors where found,
     * otherwise, redirects to the dashboard.
     */
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
//        //Grab the data entered by the user.
//        String username = userForm.getUsername();
//        String password = userForm.getPassword();
//
//        //Validate the form.
//        userValidator.validate(userForm, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//
//            return "register";
//        }
//
//        //At this point, all the validation has passed. Save the new account and login the user.
//        userService.save(userForm);
//        securityService.autoLogin(username, password);
//
//        return "redirect:/dashboard";
//    }
}
