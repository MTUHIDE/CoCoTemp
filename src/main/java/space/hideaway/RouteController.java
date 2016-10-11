package space.hideaway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by dough on 9/21/2016.
 */
@Controller
public class RouteController {

    /**
     * All requests to the index location are routed to the index.html template.
     *
     * @return The name of the template to be used.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
            return "about";
    }

}
