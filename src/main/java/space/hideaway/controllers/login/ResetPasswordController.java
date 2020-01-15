package space.hideaway.controllers.login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import space.hideaway.model.User;
import space.hideaway.model.security.PasswordResetToken;
import space.hideaway.services.PasswordResetService;
import space.hideaway.services.security.SecurityService;
import space.hideaway.services.user.UserService;
import space.hideaway.services.user.UserToolsService;
import space.hideaway.repositories.PasswordTokenRepository;
import space.hideaway.repositories.UserRepository;
import java.util.Calendar;
import java.util.Locale;


@Controller
@RequestMapping("/resetPassword")
@SessionAttributes("user")
public class ResetPasswordController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final UserToolsService userToolsService;
    private final SecurityService securityService;

    @Autowired
    PasswordTokenRepository passwordTokenRepository;
    @Autowired
    public ResetPasswordController(
            SecurityService securityService,
            UserRepository userRepository,
            PasswordResetService passwordResetService,
            UserService userService,
            UserToolsService userToolsService)
    {
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
        this.userService = userService;
        this.userToolsService = userToolsService;
    }

    @RequestMapping
    public String checkToken(WebRequest request, final Model modelMap, final @RequestParam("token") String token){
        if(token ==  null){
            return "redirect:/error.html";
        }
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
        System.out.println(passwordResetToken.getToken());
        if(passwordResetToken == null){
            return "redirect:/error.html";
        }
        Calendar cal = Calendar.getInstance();
        if(passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime() <= 0){
            return "redirect:/error.html";
        }
        modelMap.addAttribute("user", new User());
        return "password/resetPassword";
        }



    @RequestMapping(method = RequestMethod.POST)
    public String CheckUser(
            @RequestParam("token") String token,
            final @ModelAttribute("password") String password,
            final @ModelAttribute("confirmationPassword") String conPass) {


        User user = passwordTokenRepository.findByToken(token).getUser();
            user.setPassword(password);
            userService.save(user);
            securityService.autoLogin(user.getUsername(), user.getPassword());
            passwordTokenRepository.delete(passwordTokenRepository.findByToken(token));
            return "redirect:/home";
    }
}
