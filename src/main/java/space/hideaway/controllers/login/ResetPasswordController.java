package space.hideaway.controllers.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.exceptions.UserNotFoundException;
import space.hideaway.model.User;
import space.hideaway.model.security.GenericResponse;
import space.hideaway.model.security.PasswordResetToken;
import space.hideaway.services.user.UserToolsService;
import space.hideaway.repositories.PasswordTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;


public class ResetPasswordController {

    private final PasswordTokenRepository passwordTokenRepository;
    // Everything here was originally in PasswordResetTo
    @Autowired
    public ResetPasswordController(PasswordTokenRepository passwordTokenRepository){
        this.passwordTokenRepository = passwordTokenRepository;
    }
    @Autowired
    private MessageSource messages;




    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
        UserToolsService userService = new UserToolsService();
        User user = userService.findUserByEmail(userEmail);
        if( user == null ) {
            throw new UserNotFoundException("User could not be found by email");
        }

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.send(constructResetTokenEmail("temptext", request.getLocale(), token, user));
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
        //return new GenericResponse("Temporary email response");
    }

    private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        String message = messages.getMessage("message.resetPassword", null, locale);
        //String message = "Temporary body!";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        // Might not be ok since it isn't in email format.
        email.setFrom("cocotemp@nau.edu");
        return email;
    }

    // Step 5:

    public String showChangePasswordPage(Locale locale, Model model, @RequestParam("id") long id,
                                         @RequestParam("token") String token) {

        // This is how the original statement was set up. A securityService class may need to be
        // created in the future when properly organizing where the various methods in this class
        // should actually be placed.
        //String result = securityService.validatePasswordResetToken(id, token);
        String result = validatePasswordResetToken(id, token);

        if(result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    }

    public String validatePasswordResetToken(long id, String token) {
        // Will have to create the token repository as passwordTokenRepository does not currently exist.
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        return null;
    }
}
