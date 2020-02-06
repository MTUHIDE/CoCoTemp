package space.hideaway.controllers.registration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import space.hideaway.model.User;
import space.hideaway.model.security.VerificationToken;
import space.hideaway.services.security.SecurityService;
import space.hideaway.services.user.UserService;
import space.hideaway.services.user.UserToolsService;

import java.util.Calendar;

@Controller
public class EmailVerificationController {
    private UserToolsService userToolsService;
    private UserService userService;
    private final SecurityService securityService;


    @Autowired
    public EmailVerificationController(UserToolsService userToolsService, UserService userService, SecurityService securityService){
        this.userToolsService= userToolsService;
        this.userService=userService;
        this.securityService=securityService;
    }

    @RequestMapping(value = "/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        VerificationToken verificationToken = userToolsService.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("error","Error 404: Resource Not Found");
            return "error";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("error","Error 500: Link Expired");
            return "error";
        }
        user.setEnabled(true);
        userToolsService.deleteVerificationToken(verificationToken);
        userService.update(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());
        return "redirect:/home";
    }
}