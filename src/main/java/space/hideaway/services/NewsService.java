package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import space.hideaway.model.News;
import space.hideaway.repositories.NewsRepository;

import java.util.Date;
import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository)
    {
        this.newsRepository = newsRepository;
    }

    public News save(News news)
    {
        news.setDateTime(new Date());
        newsRepository.save(news);
        return news;
    }

    public List<News> getAll()
    {
        return newsRepository.findAll(Sort.by("id").descending());
    }
}
