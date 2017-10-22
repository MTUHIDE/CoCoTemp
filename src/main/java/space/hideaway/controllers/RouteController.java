package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import space.hideaway.repositories.NewsRepository;

/**
 * Edited by Justin Havely
 * 8/18/17
 *
 * Serves the home page and other general pages.
 */
@Controller
public class RouteController
{

    private final NewsRepository newsRepository;

    @Autowired
    public RouteController(NewsRepository newsRepository)
    {
        this.newsRepository = newsRepository;
    }

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
        model.addAttribute("articles", newsRepository.topNews());
        return "index";
    }

    /**
     * The endpoint for the application acknowledgment page.
     * Secured: No
     * Method: GET
     * <p>
     * Sample URL: /about
     *
     * @return The path to the about page template.
     */
    @GetMapping("/acknowledgment")
    public String acknowledgment()
    {
        return "acknowledgment";
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
