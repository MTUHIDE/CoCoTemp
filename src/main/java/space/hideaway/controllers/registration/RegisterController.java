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
import space.hideaway.services.security.SecurityService;
import space.hideaway.services.user.UserService;
import space.hideaway.validation.PersonalDetailsValidator;
import space.hideaway.validation.UserAccountValidator;

/**
 * Edited by Justin Havely
 * 8/18/17
 *
 * Serves the registration pages to the user. Contains a session attribute "user"
 * to persist a template user across the multiple pages of the registration route.
 * Route: register -> questionPage -> dashboard
 */
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

    /**
     * The mapping for the first page (register) of the registration form. This page
     * contains the Email, Username, Password, and confirm password fields.
     *
     * @param modelMap The Spring model for the registration page.
     * @return The template for the register page.
     */
    @RequestMapping
    public String initialPage(final ModelMap modelMap)
    {
        modelMap.addAttribute("user", new User());
        return "registration/register";
    }

    /**
     * The mapping for the second page (question) of the registration form. This page
     * contains the First, Middle initial, and Last name fields.
     *
     * @param user The user that exists in the model with fields populated by what the user
     *             entered into the form on the initial page (register).
     * @param bindingResult The error module for this page, allows bindings between the validation module.
     * @return The template for the question page.
     */
    @RequestMapping(params = "_question", method = RequestMethod.POST)
    public String questionPage(
            final @ModelAttribute("user") User user,
            final BindingResult bindingResult)
    {
        // Checks Email, Username, Password, and confirm password fields.
        userAccountValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            return "registration/register";
        }

        return "registration/questionPage";
    }

    /**
     * The mapping for the final step of the registration route.
     * Validation is performed on fields from the previous page
     * and the user is persisted if no issues are present.
     *
     * @param user The user that exists in the model with fields populated by what the user
     *             entered into the form on the question page.
     * @param bindingResult The error module for this page, allows bindings between the validation module.
     * @param sessionStatus The session module for the registration route.
     * @return A redirect command to the dashboard, or the path to the site-questionnaire if validation
     *         has failed on fields from the previous page.
     */
    @RequestMapping(params = "_finish", method = RequestMethod.POST)
    public String processFinish(
            final @ModelAttribute("user") User user,
            final BindingResult bindingResult,
            final SessionStatus sessionStatus)
    {
        String username = user.getUsername();
        String password = user.getPassword();

        // Validates the First, Middle initial, and Last name fields.
        personalDetailsValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            return "registration/questionPage";
        }

        // At this point, all the validation has passed. Save the new account and login the user.
        userService.save(user);
        // Auto login, so the user does not have to login after creating an account.
        securityService.autoLogin(username, password);

        sessionStatus.setComplete();
        return "redirect:dashboard";
    }

    /**
     * The mapping if the registration is not completed. Closes the registration session.
     *
     * @param sessionStatus The session module for the registration route.
     * @return The template for the home page.
     */
    @RequestMapping(params = "_cancel", method = RequestMethod.POST)
    public String processCancel(final SessionStatus sessionStatus)
    {
        sessionStatus.setComplete();
        return "index";
    }

}
