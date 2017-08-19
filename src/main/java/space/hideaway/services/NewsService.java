package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import space.hideaway.model.News;
import space.hideaway.repositories.NewsRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Justin Havely
 * 7/7/17
 */
@Service
public class NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository)
    {
        this.newsRepository = newsRepository;
    }

    /**
     * Saves a news post into the database. Automatically sets the time the news post was posted.
     *
     * @param news The news post to save
     * @return The saved news post
     */
    public News save(News news)
    {
        news.setDateTime(new Date());
        newsRepository.save(news);
        return news;
    }

    /**
     * Gets all of the news post in the database.
     *
     * @return All news posts in the database
     */
    public List<News> getAll()
    {
        return newsRepository.findAll(Sort.by("id").descending());
    }
}
