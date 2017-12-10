package space.hideaway.controllers.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.model.User;
import space.hideaway.model.security.GenericResponse;
import space.hideaway.services.user.UserServiceImplementation;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    /**
     * The default mapping, returns the forgotPassword page.
     * @return The path to the forgotPassword template.
     */
    @RequestMapping
    public String showForgotPassword(){ return "login/forgotPassword";}

    }
