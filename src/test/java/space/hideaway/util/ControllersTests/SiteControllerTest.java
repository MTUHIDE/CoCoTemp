package space.hideaway.util.ControllersTests;
import org.junit.*;
import org.springframework.ui.Model;
import space.hideaway.controllers.site.SiteController;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;
import space.hideaway.services.site.SiteService;
import space.hideaway.services.site.SiteStatisticsService;
import space.hideaway.services.user.UserService;
import space.hideaway.util.FormatUtils;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SiteControllerTest {
   private SiteController siteController;
   private SiteService MockSiteService;
   private SiteStatisticsService MockSiteStatisticsService;
   private  UserService MockUserService;

    @Before
        public void setUp(){
        MockSiteService = mock(SiteService.class);
        MockSiteStatisticsService = mock(SiteStatisticsService.class);
        MockUserService = mock(UserService.class);
        siteController = new SiteController(MockSiteService,MockSiteStatisticsService,MockUserService);
    }

    @Test
        public void TestShowSite(){
        double expectedMax = 13;
        double expectedMin = 1;
        double expectedAvg = 5;
        double expectedSTD = 3;

        double expectedMaxF=55.4;
        double expectedMinF = 33.8;
        double expectedAvgF = 41;
        double expectedSTDF = 37.4;
        String expectedMaxString = FormatUtils.doubleToVisualString(expectedMaxF);
        String expectedMinString = FormatUtils.doubleToVisualString(expectedMinF);
        String expectedAvgString = FormatUtils.doubleToVisualString(expectedAvgF);
        String expectedSTDString = FormatUtils.doubleToVisualString(expectedSTDF);

        long expectedLong = (long) 456;
        UUID expectedUUID = new UUID(expectedLong,expectedLong);


        Model MockModel = mock(Model.class);
        Site MockSite = mock(Site.class);
        SiteStatistics MockSiteStatistics = mock(SiteStatistics.class);
        User MockUser = mock(User.class);

        when(MockSiteService.findByKey(expectedUUID.toString())).thenReturn(MockSite);
        when(MockSite.getId()).thenReturn(expectedUUID);
        when(MockSite.getUser()).thenReturn(MockUser);
        when(MockSiteStatisticsService.getMostRecent(MockSite)).thenReturn(MockSiteStatistics);
        when(MockSiteStatistics.getAllMax()).thenReturn(expectedMax);
        when(MockSiteStatistics.getAllMin()).thenReturn(expectedMin);
        when(MockSiteStatistics.getAllAvg()).thenReturn(expectedAvg);
        when(MockSiteStatistics.getAllDeviation()).thenReturn(expectedSTD);



        Assert.assertEquals("station",siteController.showSite(MockModel,expectedUUID));
        verify(MockModel).addAttribute("site",MockSite);
        verify(MockModel).addAttribute("siteID",expectedUUID);
        verify(MockModel).addAttribute("user",MockUser);
        verify(MockModel).addAttribute("tempstandard",'F');
        verify(MockModel).addAttribute("max",expectedMaxString);
        verify(MockModel).addAttribute("min",expectedMinString);
        verify(MockModel).addAttribute("avg",expectedAvgString);
        verify(MockModel).addAttribute("deviation",expectedSTDString);


    }
}
