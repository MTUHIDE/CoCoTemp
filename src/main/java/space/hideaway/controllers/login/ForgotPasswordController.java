package space.hideaway.controllers.login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.User;
import space.hideaway.model.security.PasswordResetToken;
import space.hideaway.repositories.PasswordTokenRepository;
import space.hideaway.services.user.*;
import space.hideaway.services.PasswordResetService;
import org.springframework.web.context.request.WebRequest;
import java.util.UUID;

@Controller
@RequestMapping("/forgotPassword")
@SessionAttributes("user")
public class ForgotPasswordController {

    private final PasswordResetService passwordResetService;
    private final UserToolsService userToolsService;
    @Autowired
    PasswordTokenRepository passwordTokenRepository;
    @Autowired
    public ForgotPasswordController(
            PasswordResetService passwordResetService,
            UserService userService,
            UserToolsService userToolsService)
    {
        this.passwordResetService = passwordResetService;
        this.userToolsService = userToolsService;
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


    @RequestMapping(params = "_reset", method = RequestMethod.POST)
    public String CheckUser(
            final @ModelAttribute("email") String email,
            final @ModelAttribute("user") User userCheck,
            final WebRequest request) {
             User user = userToolsService.findUserByEmail(email);
                 createToken(user, request);
                 return "password/passwordSent" ;
    }



    public void createToken(User user, WebRequest request){
    String token = UUID.randomUUID().toString();
    PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
    passwordTokenRepository.save(passwordResetToken);
    passwordResetService.sendPasswordRestEmail(user, request.getContextPath(),token);
    }

}
