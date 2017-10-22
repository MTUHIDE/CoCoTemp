package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.News;
import space.hideaway.services.NewsService;


/**
 * Created by Justin Havely
 * 6/27/2017
 *
 * Serves the news pages to the user.
 */
@Controller
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService)
    {
        this.newsService = newsService;
    }

    /**
     * The endpoint for the news post page.
     * Secured: yes
     * Method: POST
     *
     * Sample URL: /news_post
     * @param news The news that exists in the model with fields populated by what the user
     *             entered into the form.
     * @return A redirect command to the home page.
     */
    @RequestMapping(value = "/news_post", method = RequestMethod.POST)
    public String newsPost(@ModelAttribute ("post") News news)
    {
        newsService.save(news);
        return "redirect:/";
    }

    /**
     * The endpoint for the news post page. This page
     * contains the title and content fields.
     * Secured: yes
     * Method: GET
     *
     * Sample URL: /news_post
     * @param model The model maintained by Spring for a news.
     * @return The path to the news template.
     */
    @GetMapping("/news_post")
    public String news(Model model)
    {
        model.addAttribute("post", new News());
        return "news/newsPost";
    }

    /**
     * The endpoint for the news history page.
     * Secured: NO
     * Method: GET
     *
     * Sample URL: /news_history
     * @param model The model maintained by Spring for a news.
     * @return The path to the news history template.
     */
    @GetMapping({"/news_history"})
    public String index(Model model)
    {
        // Adds every news post in the database from newest to oldest.
        model.addAttribute("articles", newsService.getAll());
        return "news/newsHistory";
    }

}
