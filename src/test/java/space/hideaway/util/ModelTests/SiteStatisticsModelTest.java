package space.hideaway.util.ModelTests;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SiteStatisticsModelTest {

    SiteStatistics siteStatistics;

    @Before
        public void setUp(){

        siteStatistics = new SiteStatistics();
    }

    @Test
        public void TestGetAndSetStatisticsId(){
        UUID expectedUUID = new UUID((long)435, (long )456);

        siteStatistics.setStatisticsID(expectedUUID);

        Assert.assertEquals(expectedUUID, siteStatistics.getStatisticsID());

    }

    @Test
    public void TestGetAndSetSiteId(){
        UUID expectedUUID = new UUID((long)435, (long )456);

        siteStatistics.setSiteID(expectedUUID);

        Assert.assertEquals(expectedUUID, siteStatistics.getSiteID());

    }

    @Test
    public void TestGetAndSetSite(){
        Site  MockSite = mock(Site.class);

        siteStatistics.setSite(MockSite);

        Assert.assertEquals(MockSite, siteStatistics.getSite());

    }

    @Test
    public void TestGetAndSetDate(){
        Date date = new Date();

        siteStatistics.setDate(date);

        Assert.assertEquals(date, siteStatistics.getDate());

    }

    @Test
    public void TestGetAndWeekMax(){
        double expectedWeekMax = 3.5;

        siteStatistics.setWeekMax(expectedWeekMax);

        Assert.assertEquals(expectedWeekMax, siteStatistics.getWeekMax(),0.0);

    }
    @Test
    public void TestGetAndWeekMin(){
        double expectedWeekMin = 3.5;

        siteStatistics.setWeekMin(expectedWeekMin);

        Assert.assertEquals(expectedWeekMin, siteStatistics.getWeekMin(),0.0);

    }

    @Test
    public void TestGetAndWeekAvg(){
        double expectedWeekAvg = 3.5;

        siteStatistics.setWeekAvg(expectedWeekAvg);

        Assert.assertEquals(expectedWeekAvg, siteStatistics.getWeekAvg(),0.0);

    }

    @Test
    public void TestGetAndWeekDeviation(){
        double expectedWeekDeviation = 3.5;

        siteStatistics.setWeekDeviation(expectedWeekDeviation);

        Assert.assertEquals(expectedWeekDeviation, siteStatistics.getWeekDeviation(),0.0);

    }

    @Test
    public void TestGetAndMonthMax(){
        double expectedMonthMax= 3.5;

        siteStatistics.setMonthMax(expectedMonthMax);

        Assert.assertEquals(expectedMonthMax, siteStatistics.getMonthMax(),0.0);

    }

    @Test
    public void TestGetAndMonthMin(){
        double expectedMonthMin= 3.5;

        siteStatistics.setMonthMin(expectedMonthMin);

        Assert.assertEquals(expectedMonthMin, siteStatistics.getMonthMin(),0.0);

    }

    @Test
    public void TestGetAndMonthAvg(){
        double expectedMonthAvg= 3.5;

        siteStatistics.setMonthAvg(expectedMonthAvg);

        Assert.assertEquals(expectedMonthAvg, siteStatistics.getMonthAvg(),0.0);

    }

    @Test
    public void TestGetAndMonthDeviation(){
        double expectedMonthDeviation= 3.5;

        siteStatistics.setMonthDeviation(expectedMonthDeviation);

        Assert.assertEquals(expectedMonthDeviation, siteStatistics.getMonthDeviation(),0.0);

    }

    @Test
    public void TestGetAndYearMax(){
        double expectedyearMax= 3.5;

        siteStatistics.setYearMax(expectedyearMax);

        Assert.assertEquals(expectedyearMax, siteStatistics.getYearMax(),0.0);

    }

    @Test
    public void TestGetAndyearMin(){
        double expectedYearMin= 3.5;

        siteStatistics.setYearMin(expectedYearMin);

        Assert.assertEquals(expectedYearMin, siteStatistics.getYearMin(),0.0);

    }

    @Test
    public void TestGetAndYearAvg(){
        double expectedYearAvg= 3.5;

        siteStatistics.setYearAvg(expectedYearAvg);

        Assert.assertEquals(expectedYearAvg, siteStatistics.getYearAvg(),0.0);

    }

    @Test
    public void TestGetAndYearDeviation(){
        double expectedyearDeviation= 3.5;

        siteStatistics.setYearDeviation(expectedyearDeviation);

        Assert.assertEquals(expectedyearDeviation, siteStatistics.getYearDeviation(),0.0);

    }

    @Test
    public void TestGetAndAllMax(){
        double expectedAllMax= 3.5;

        siteStatistics.setAllMax(expectedAllMax);

        Assert.assertEquals(expectedAllMax, siteStatistics.getAllMax(),0.0);

    }

    @Test
    public void TestGetAndAllMin(){
        double expectedAllMin= 3.5;

        siteStatistics.setAllMin(expectedAllMin);

        Assert.assertEquals(expectedAllMin, siteStatistics.getAllMin(),0.0);

    }

    @Test
    public void TestGetAndAllAvg(){
        double expectedAllAvg= 3.5;

        siteStatistics.setAllAvg(expectedAllAvg);

        Assert.assertEquals(expectedAllAvg, siteStatistics.getAllAvg(),0.0);

    }

    @Test
    public void TestGetAndAllDeviation(){
        double expectedAllDeviation= 3.5;

        siteStatistics.setAllDeviation(expectedAllDeviation);

        Assert.assertEquals(expectedAllDeviation, siteStatistics.getAllDeviation(),0.0);

    }

    @Test
    public void TestSetWeek(){
        SummaryStatistics MockWeek = mock(SummaryStatistics.class);

        double expectedMin = 3.69;
        double expectedMax = 5.9;
        double expectedAvg = 3.2;
        double expectedDeviation = 127.65;
        double expectedRoundMin = 3.7;


        when(MockWeek.getMin()).thenReturn(expectedMin);
        when(MockWeek.getMax()).thenReturn(expectedMax);
        when(MockWeek.getMean()).thenReturn(expectedAvg);
        when(MockWeek.getStandardDeviation()).thenReturn(expectedDeviation);

        siteStatistics.setWeek(MockWeek);

        Assert.assertEquals(expectedRoundMin, siteStatistics.getWeekMin(),0.0);
        Assert.assertEquals(expectedMax, siteStatistics.getWeekMax(),0.0);
        Assert.assertEquals(expectedAvg, siteStatistics.getWeekAvg(),0.0);
        Assert.assertEquals(expectedDeviation, siteStatistics.getWeekDeviation(),0.0);


    }
    @Test
    public void TestSetMonth(){
        SummaryStatistics MockMonth = mock(SummaryStatistics.class);

        double expectedMin = 3.69;
        double expectedMax = 5.9;
        double expectedAvg = 3.2;
        double expectedDeviation = 127.65;
        double expectedRoundMin = 3.7;


        when(MockMonth.getMin()).thenReturn(expectedMin);
        when(MockMonth.getMax()).thenReturn(expectedMax);
        when(MockMonth.getMean()).thenReturn(expectedAvg);
        when(MockMonth.getStandardDeviation()).thenReturn(expectedDeviation);

        siteStatistics.setMonth(MockMonth);

        Assert.assertEquals(expectedRoundMin, siteStatistics.getMonthMin(),0.0);
        Assert.assertEquals(expectedMax, siteStatistics.getMonthMax(),0.0);
        Assert.assertEquals(expectedAvg, siteStatistics.getMonthAvg(),0.0);
        Assert.assertEquals(expectedDeviation, siteStatistics.getMonthDeviation(),0.0);


    }

    @Test
    public void TestSetYear(){
        SummaryStatistics MockYear = mock(SummaryStatistics.class);

        double expectedMin = 3.69;
        double expectedMax = 5.9;
        double expectedAvg = 3.2;
        double expectedDeviation = 127.65;
        double expectedRoundMin = 3.7;


        when(MockYear.getMin()).thenReturn(expectedMin);
        when(MockYear.getMax()).thenReturn(expectedMax);
        when(MockYear.getMean()).thenReturn(expectedAvg);
        when(MockYear.getStandardDeviation()).thenReturn(expectedDeviation);

        siteStatistics.setYear(MockYear);

        Assert.assertEquals(expectedRoundMin, siteStatistics.getYearMin(),0.0);
        Assert.assertEquals(expectedMax, siteStatistics.getYearMax(),0.0);
        Assert.assertEquals(expectedAvg, siteStatistics.getYearAvg(),0.0);
        Assert.assertEquals(expectedDeviation, siteStatistics.getYearDeviation(),0.0);


    }

    @Test
    public void TestSetAll(){
        SummaryStatistics MockAll = mock(SummaryStatistics.class);

        double expectedMin = 3.69;
        double expectedMax = 5.9;
        double expectedAvg = 3.2;
        double expectedDeviation = 127.65;
        double expectedRoundMin = 3.7;


        when(MockAll.getMin()).thenReturn(expectedMin);
        when(MockAll.getMax()).thenReturn(expectedMax);
        when(MockAll.getMean()).thenReturn(expectedAvg);
        when(MockAll.getStandardDeviation()).thenReturn(expectedDeviation);

        siteStatistics.setAll(MockAll);

        Assert.assertEquals(expectedRoundMin, siteStatistics.getAllMin(),0.0);
        Assert.assertEquals(expectedMax, siteStatistics.getAllMax(),0.0);
        Assert.assertEquals(expectedAvg, siteStatistics.getAllAvg(),0.0);
        Assert.assertEquals(expectedDeviation, siteStatistics.getAllDeviation(),0.0);


    }
}
