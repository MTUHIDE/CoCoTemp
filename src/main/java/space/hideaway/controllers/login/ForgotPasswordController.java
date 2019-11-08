package space.hideaway.controllers.login;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.User;
import space.hideaway.model.security.GenericResponse;
import space.hideaway.services.user.UserServiceImplementation;
import space.hideaway.repositories.UserRepository;
import space.hideaway.services.user.UserServiceImplementation;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/forgotPassword")
@SessionAttributes("user")
public class ForgotPasswordController {
    /**
     * The default mapping, returns the forgotPassword page.
     *
     * @return The path to the forgotPassword template.
     */
    @RequestMapping
    public String showForgotPassword(final ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        return "login/forgotPassword";
    }


    @RequestMapping(params = "_email", method = RequestMethod.POST)
    public void CheckUser() {

    }

}