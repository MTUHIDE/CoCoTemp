package space.hideaway.controllers.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import space.hideaway.model.User;
import space.hideaway.services.SecurityService;
import space.hideaway.services.UserService;
import space.hideaway.validation.PersonalDetailsValidator;
import space.hideaway.validation.UserAccountValidator;


@Controller
@RequestMapping("/register")
@SessionAttributes("user")
public class RegisterController
{

    private final UserService userService;
    private final SecurityService securityService;
    private final UserAccountValidator userAccountValidator;
    private final PersonalDetailsValidator personalDetailsValidator;

    @Autowired
    public RegisterController(
            SecurityService securityService,
            UserAccountValidator userAccountValidator,
            UserService userService,
            PersonalDetailsValidator personalDetailsValidator)
    {
        this.securityService = securityService;
        this.userAccountValidator = userAccountValidator;
        this.userService = userService;
        this.personalDetailsValidator = personalDetailsValidator;
    }


    @RequestMapping
    public String initialPage(final ModelMap modelMap)
    {
        modelMap.addAttribute("user", new User());
        return "registration/register";
    }

    @RequestMapping(params = "_question", method = RequestMethod.POST)
    public String questionPage(
            final @ModelAttribute("user") User user,
            final BindingResult bindingResult)
    {
        userAccountValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            return "registration/register";
        }

        return "registration/questionPage";
    }

    @RequestMapping(params = "_finish", method = RequestMethod.POST)
    public String processFinish(
            final @ModelAttribute("user") User user,
            final BindingResult bindingResult,
            final SessionStatus sessionStatus)
    {
        String username = user.getUsername();
        String password = user.getPassword();

        // Validates the form.
        personalDetailsValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            return "registration/questionPage";
        }

        // At this point, all the validation has passed. Save the new account and login the user.
        userService.save(user);
        securityService.autoLogin(username, password);

        sessionStatus.setComplete();
        return "redirect:dashboard";
    }

    @RequestMapping(params = "_cancel", method = RequestMethod.POST)
    public String processCancel(final SessionStatus sessionStatus)
    {
        sessionStatus.setComplete();
        return "index";
    }

}
