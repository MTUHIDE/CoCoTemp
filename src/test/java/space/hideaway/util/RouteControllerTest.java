package space.hideaway.util;

import org.junit.*;
import org.springframework.ui.Model;
import space.hideaway.controllers.RouteController;
import space.hideaway.model.News;
import space.hideaway.repositories.NewsRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RouteControllerTest {

    NewsRepository MockNewsRepository ;
    RouteController routeController;
    @Before
        public void setUp(){
        MockNewsRepository = mock(NewsRepository.class);
        routeController = new RouteController(MockNewsRepository);
    }

    @Test
        public void TestIndex(){
        Model MockModel = mock(Model.class);
        List<News> newsList = new ArrayList<News>();
        News MockNews = mock( News.class);
        newsList.add(MockNews);
        when(MockNewsRepository.topNews()).thenReturn(newsList);

        Assert.assertEquals("index",routeController.index(MockModel));

        verify(MockModel).addAttribute("articles",newsList);
    }

    @Test
        public void TestAcknowledgment(){
        Assert.assertEquals("acknowledgment",routeController.acknowledgment());

    }
    @Test
    public void TestContact(){
        Assert.assertEquals("contact",routeController.contact());

    }
    @Test
    public void TestGetting_Started(){
        Assert.assertEquals("getting_started",routeController.getting_started());

    }
    @Test
    public void TestExternal_Resources(){
        Assert.assertEquals("external_resources",routeController.external_resources());

    }
    @Test
    public void TestMicroClimate(){
        Assert.assertEquals("microclimate",routeController.microclimate());

    }
}
