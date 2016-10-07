package space.hideaway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by caden on 10/7/2016.
 */

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
