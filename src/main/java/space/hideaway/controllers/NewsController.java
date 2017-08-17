package space.hideaway.controllers;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.News;
import space.hideaway.repositories.NewsRepository;
import space.hideaway.services.NewsService;

import java.util.Date;

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
     * Method: Post
     * <p>
     * Sample URL: /news_post
     *
     * @return The name of the news post page template.
     */
    @RequestMapping(value = "/news_post", method = RequestMethod.POST)
    public String newsPost(@ModelAttribute ("post") News news)
    {
        newsService.save(news);
        return "redirect:/";
    }

    @GetMapping("/news_post")
    public String news(Model model)
    {
        model.addAttribute("post", new News());
        return "news/newsPost";
    }

    @GetMapping({"/news_history"})
    public String index(Model model)
    {
        model.addAttribute("articles", newsService.getAll());
        return "news/newsHistory";
    }

}
