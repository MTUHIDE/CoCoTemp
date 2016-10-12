package space.hideaway.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UI model for basic static routes that contain no other logic than to display a template.
 *
 * Created by dough on 9/21/2016.
 * Enhanced by caden on 10/09/2016.
 */
@Controller
public class RouteController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/appLogin")
    public String appLogin() {
        return "appLogin";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/manage")
    public String manage() {return "manage";}

}
