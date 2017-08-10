package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import space.hideaway.model.News;
import space.hideaway.repositories.NewsRepository;

import java.util.Date;

@Controller
public class NewsController {
    @Autowired
    NewsRepository newsRepository;

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
    public String newsPost(@RequestParam(value = "title") String title,
                           @RequestParam(value = "content") String content) {
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
        return "news/newsPost";
    }

    @GetMapping({"/news_history"})
    public String index(Model model) {
        model.addAttribute("articles", newsRepository.findAll(Sort.by("id").descending()));
        return "news/newsHistory";
    }

}
