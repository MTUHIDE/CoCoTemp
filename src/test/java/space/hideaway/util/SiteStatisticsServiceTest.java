package space.hideaway.util;

import org.junit.*;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;
import space.hideaway.repositories.site.SiteStatisticsRepository;
import space.hideaway.services.data.DataService;
import space.hideaway.services.site.SiteStatisticsService;
import space.hideaway.services.site.SiteStatisticsServiceImplementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SiteStatisticsServiceTest {

    SiteStatisticsService siteStatisticsService ;
    SiteStatisticsRepository mockSiteStatisticsRepository;
    DataService mockDataService;

    @Before
    public void setUp(){
        mockDataService = mock(DataService.class);
        mockSiteStatisticsRepository = mock(SiteStatisticsRepository.class);
        siteStatisticsService = new SiteStatisticsServiceImplementation(mockSiteStatisticsRepository,mockDataService);

    }
//TODO: See if it is possible to recalculateStatistics

    @Test
    public void testSave(){
        SiteStatistics mockSiteStatistics = mock(SiteStatistics.class);
        when(mockSiteStatisticsRepository.save(mockSiteStatistics)).thenReturn(mockSiteStatistics);

        Assert.assertEquals(mockSiteStatistics, siteStatisticsService.save(mockSiteStatistics));
    }

    @Test
    public void testGetMostRecentNotEmpty(){
        Site mockSite = mock(Site.class);

        List<SiteStatistics> siteStatisticsList = new ArrayList<SiteStatistics>();
        SiteStatistics mockSiteStatistics = new SiteStatistics();
        SiteStatistics mockSiteStatistics2 = new SiteStatistics();

        Date firstDate = new Date(1997,2,2);
        Date secondDate = new Date(1999,2,4);
        mockSiteStatistics.setDate(firstDate);
        mockSiteStatistics2.setDate(secondDate);

        siteStatisticsList.add(mockSiteStatistics);
        siteStatisticsList.add(mockSiteStatistics2);


        when(mockSite.getSiteStatisticsList()).thenReturn(siteStatisticsList);

        Assert.assertEquals(mockSiteStatistics2,siteStatisticsService.getMostRecent(mockSite));

    }

    @Test
    public void testGetMostRecentEmpty(){
        Site mockSite = mock(Site.class);

        List<SiteStatistics> siteStatisticsList = new ArrayList<SiteStatistics>();
        when(mockSite.getSiteStatisticsList()).thenReturn(siteStatisticsList);


        Assert.assertEquals(SiteStatistics.EMPTY_STATISTIC,siteStatisticsService.getMostRecent(mockSite));
    }




}
