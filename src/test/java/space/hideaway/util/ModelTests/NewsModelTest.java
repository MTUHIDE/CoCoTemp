package space.hideaway.util.ModelTests;
import org.junit.*;
import space.hideaway.model.News;

import java.util.Date;

public class NewsModelTest {

    News news;
    @Before
        public void setUp(){
        news = new News();
    }

    @Test
        public void TestGetAndSetId(){
        long expectedId = (long)3432;

        news.setId(expectedId);


        Assert.assertEquals(expectedId,news.getId());
    }

    @Test
    public void TestGetAndSetDateTime(){
        Date  expectedDate = new Date();

        news.setDateTime(expectedDate);


        Assert.assertEquals(expectedDate,news.getDateTime());
    }

    @Test
    public void TestGetAndSetTitle(){
        String expectedTitle = "title";

        news.setTitle(expectedTitle);


        Assert.assertEquals(expectedTitle,news.getTitle());
    }
    @Test
    public void TestGetAndSetContent(){
        String expectedContent = "content";

        news.setContent(expectedContent);


        Assert.assertEquals(expectedContent,news.getContent());
    }
}
