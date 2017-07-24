package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.model.Device;
import space.hideaway.model.News;
import space.hideaway.model.User;
import space.hideaway.repositories.NewsRepository;
import space.hideaway.services.UserService;

import java.util.Date;


@Controller
public class RouteController
{
    @Autowired
    NewsRepository newsRepository;
    UserService userService;

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

    /**
     * The endpoint for the news post page.
     * Secured: yes
     * Method: GET
     * <p>
     * Sample URL: /news_post
     *
     * @return The name of the news post page template.
     */
    @RequestMapping(value = "/news_post", method = RequestMethod.POST)
    public String newsPost(@RequestParam(value = "title") String title,
                           @RequestParam(value = "content") String content)
    {
        News news = new News();
        news.setContent(content);
        news.setTitle(title);
        news.setDateTime(new Date());
        newsRepository.save(news);

        return "redirect:/";
    }

    @GetMapping("/news_post")
    public String news()
    {
        return "/newsPost";
    }
}
