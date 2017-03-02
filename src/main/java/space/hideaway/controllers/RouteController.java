package space.hideaway.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.model.SearchModel;


@Controller
public class RouteController
{

    /**
     * The endpoint for the application home page.
     * Secured: No
     * Method: GET
     * <p>
     * Sample URL: / or /home
     *
     * @return The path to the index page template.
     */
    @GetMapping({"/", "/home"})
    public String index(Model model)
    {
        model.addAttribute("searchModel", new SearchModel());
        return "index";
    }

    /**
     * The endpoint for the application about page.
     * Secured: No
     * Method: GET
     * <p>
     * Sample URL: /about
     *
     * @return The path to the about page template.
     */
    @GetMapping("/about")
    public String about()
    {
        return "about";
    }

    /**
     * The endpoint for the contact page.
     * Secured: No
     * Method: GET
     * <p>
     * Sample URL: /contact
     *
     * @return The name of the contact page template.
     */
    @GetMapping("/contact")
    public String contact()
    {
        return "contact";
    }
}
