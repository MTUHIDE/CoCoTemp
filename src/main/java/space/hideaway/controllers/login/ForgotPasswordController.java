package space.hideaway.controllers.login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.User;
import space.hideaway.model.security.PasswordResetToken;
import space.hideaway.repositories.PasswordTokenRepository;
import space.hideaway.services.user.*;
import space.hideaway.services.PasswordResetService;
import org.springframework.web.context.request.WebRequest;
import space.hideaway.validation.UserAccountValidator;
import java.util.UUID;

@Controller
@RequestMapping("/forgotPassword")
@SessionAttributes("user")
public class ForgotPasswordController {

    private final PasswordResetService passwordResetService;
    private final UserToolsService userToolsService;
    private final UserAccountValidator userAccountValidator;
    @Autowired
    PasswordTokenRepository passwordTokenRepository;
    @Autowired
    public ForgotPasswordController(
            PasswordResetService passwordResetService,
            UserService userService,
            UserToolsService userToolsService,
            UserAccountValidator userAccountValidator)
    {
        this.passwordResetService = passwordResetService;
        this.userToolsService = userToolsService;
        this.userAccountValidator = userAccountValidator;
    }


    /**
     * The default mapping, returns the forgotPassword page.
     *
     * @return The path to the forgotPassword template.
     */
    @RequestMapping
    public String showForgotPassword(final ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("email", new String());
        return "password/forgotPassword";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String CheckUser(
            final @ModelAttribute("user") User user,
            final WebRequest request,
            final BindingResult bindingResult){

            //Checks if the email is valid
            userAccountValidator.validateEmail(user, bindingResult);
            if(bindingResult.hasErrors()){
                return "password/forgotPassword";
            }
                //If email is valid, finds user by email, creates a password reset token and send it to their email
                User currentUser = userToolsService.findUserByEmail(user.getEmail());
                createToken(currentUser, request);
                 return "password/passwordSent" ;
    }



    public void createToken(User user, WebRequest request){
    String token = UUID.randomUUID().toString();
    PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
    passwordTokenRepository.save(passwordResetToken);
    passwordResetService.sendPasswordRestEmail(user, request.getContextPath(),token);
    }

}
