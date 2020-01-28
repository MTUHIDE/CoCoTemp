package space.hideaway.util.ControllersTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import space.hideaway.controllers.NewsController;
import space.hideaway.model.News;
import space.hideaway.services.NewsService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


public class NewsControllerTest {
    NewsService MockNewsService;

    NewsController newsController;

    @Before
        public void setUp(){
        MockNewsService = mock(NewsService.class);
        newsController = new NewsController(MockNewsService);
    }

    @Test
        public void TestNewsPost(){
        News MockNews= mock(News.class);
        when(MockNewsService.save(MockNews)).thenReturn(MockNews);
        Assert.assertEquals("redirect:/",newsController.newsPost(MockNews));
    }

    @Test
        public void TestNews(){
        Model MockModel = mock(Model.class);
        Assert.assertEquals("news/newsPost",newsController.news(MockModel));
    }

    @Test
        public void TestIndex(){
        Model MockModel = mock(Model.class);
        News MockNews = mock(News.class);
        List<News> newsList = new ArrayList<News>();
        newsList.add(MockNews);
        when(MockNewsService.getAll()).thenReturn(newsList);

        Assert.assertEquals("news/newsHistory",newsController.index(MockModel));
        verify(MockModel).addAttribute("articles",newsList);

    }

}
