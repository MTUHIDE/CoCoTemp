package space.hideaway.util;
import javafx.scene.chart.PieChart;
import org.junit.*;
import org.mockito.Mock;
import space.hideaway.controllers.RESTController;
import space.hideaway.model.Device;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.model.upload.UploadHistory;
import space.hideaway.services.RESTService;
import space.hideaway.services.site.SiteMetadataService;
import space.hideaway.services.site.SiteService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import space.hideaway.model.Data;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RESTControllerTest {

    SiteService MockSiteService;
    RESTService MockRESTService;
    SiteMetadataService MockSiteMetaDataService;
    RESTController restController;

    @Before
        public void setUp(){
        MockSiteService = mock(SiteService.class);
        MockRESTService = mock(RESTService.class);
        MockSiteMetaDataService = mock(SiteMetadataService.class);
        restController = new RESTController(MockSiteService,MockRESTService,MockSiteMetaDataService);
    }

    @Test
        public void TestInfo(){
        Site MockSite = mock(Site.class);
        String expectedString = "CoCoTemp";
        when(MockSiteService.findByKey(expectedString)).thenReturn(MockSite);

        Assert.assertEquals(MockSite,restController.info(expectedString));
    }


    @Test
        public void TestPopulateInfoCard(){
        InfoCardSerializer MockInfoCardSerializer = mock(InfoCardSerializer.class);

        when(MockRESTService.populateInfocards()).thenReturn(MockInfoCardSerializer);

        Assert.assertEquals(MockInfoCardSerializer,restController.populateInfocard());
    }

    @Test
        public void TestPopulateSites(){
        Site MockSite = mock(Site.class);
        List<Site> siteList = new ArrayList<Site>();
        siteList.add(MockSite);
        when(MockRESTService.populateSites()).thenReturn(siteList);

        Assert.assertEquals(siteList,restController.populateSites());
    }

    @Test
        public void TestPopulateDevices(){
        Device MockDevice = mock(Device.class);
        List<Device> deviceList = new ArrayList<Device>();
        deviceList.add(MockDevice);
        when(MockRESTService.populateDevices()).thenReturn(deviceList);

        Assert.assertEquals(deviceList,restController.populateDevices());
    }
    @Test
        public void TestGetUploadHistoryNull(){
        String expectedRange = null;
        UploadHistory MockUploadHistory = mock(UploadHistory.class);
        List<UploadHistory> historyList = new ArrayList<UploadHistory>();
        historyList.add(MockUploadHistory);

        when(MockRESTService.getUploadHistory(HistoryUnit.LAST_30)).thenReturn(historyList);

        Assert.assertEquals(historyList,restController.getUploadHistory(expectedRange));
    }

    @Test
    public void TestGetUploadHistoryWEEK(){
        String expectedRange = HistoryUnit.WEEK.name();
        UploadHistory MockUploadHistory = mock(UploadHistory.class);
        List<UploadHistory> historyList = new ArrayList<UploadHistory>();
        historyList.add(MockUploadHistory);

        when(MockRESTService.getUploadHistory(HistoryUnit.WEEK)).thenReturn(historyList);

        Assert.assertEquals(historyList,restController.getUploadHistory(expectedRange));
    }

    @Test
    public void TestGetUploadHistoryLAST_30(){
        String expectedRange = HistoryUnit.LAST_30.name();
        UploadHistory MockUploadHistory = mock(UploadHistory.class);
        List<UploadHistory> historyList = new ArrayList<UploadHistory>();
        historyList.add(MockUploadHistory);

        when(MockRESTService.getUploadHistory(HistoryUnit.LAST_30)).thenReturn(historyList);

        Assert.assertEquals(historyList,restController.getUploadHistory(expectedRange));
    }
    @Test
    public void TestGetUploadHistoryYEAR(){
        String expectedRange = HistoryUnit.YEAR.name();
        UploadHistory MockUploadHistory = mock(UploadHistory.class);
        List<UploadHistory> historyList = new ArrayList<UploadHistory>();
        historyList.add(MockUploadHistory);

        when(MockRESTService.getUploadHistory(HistoryUnit.YEAR)).thenReturn(historyList);

        Assert.assertEquals(historyList,restController.getUploadHistory(expectedRange));
    }
    @Test
    public void TestGetUploadHistoryNOENUM(){
        String expectedRange = "CoCoTemp";
        UploadHistory MockUploadHistory = mock(UploadHistory.class);
        List<UploadHistory> historyList = new ArrayList<UploadHistory>();
        historyList.add(MockUploadHistory);

        when(MockRESTService.getUploadHistory(HistoryUnit.LAST_30)).thenReturn(historyList);

        Assert.assertEquals(historyList,restController.getUploadHistory(expectedRange));
    }

//    @Test
//        public void TestGetTemperaturePoints(){
//        Data MockDataOne = mock(Data.class);
//        Data MockDataTwo = mock(Data.class);
//        Set<Data> dataSet = new HashSet<Data>();
//        dataSet.add(MockDataOne);
//        dataSet.add(MockDataTwo);
//        Site MockSite  = mock(Site.class);
//        List<Data> dataList = new ArrayList<Data>();
//        dataList.add(MockDataOne);
//        dataList.add(MockDataTwo);
//
//        String expectedSiteId = "CoCoTemp";
//        when(MockSiteService.findByKey(expectedSiteId)).thenReturn(MockSite);
//        when(MockSite.getDataSet()).thenReturn(dataSet);
//        when(SortingUtils.sortMostRecentFirst(dataSet)).thenReturn(dataList);
//
//        Assert.assertEquals(dataList,restController.getTemperaturePoints(expectedSiteId));
//
//
//    }
    @Test
        public void TestGetSitePoints(){
        List<Site> siteList = new ArrayList<Site>();;
        Site mockSite = mock(Site.class);
        siteList.add(mockSite);

        when(MockSiteService.getAllSites()).thenReturn(siteList);

        Assert.assertEquals(siteList,restController.getSitePoints());

    }
    @Test
        public void TestRenderSearchWithKeywordAndSpatial(){
        String expectedQuery ="query";
        String expectedLocation = "Houghton";
        String expectedRange = "Range";
        List<Site> siteList = new ArrayList<Site>();
        Site MockSite = mock(Site.class);
        siteList.add(MockSite);
        when(MockRESTService.populateSitesByQuery(expectedQuery,expectedLocation,expectedRange)).thenReturn(siteList);

        Assert.assertEquals(siteList,restController.renderSearchWithKeywordAndSpatial(expectedQuery,expectedLocation,expectedRange));

    }

    @Test
        public void TestGetSiteMetadataPoints(){
        List<SiteMetadata> siteMetadataList = new ArrayList<SiteMetadata>();
        SiteMetadata MockSiteMetaData = mock(SiteMetadata.class);
        siteMetadataList.add(MockSiteMetaData);

        when(MockSiteMetaDataService.getAllSiteMetadata()).thenReturn(siteMetadataList);

        Assert.assertEquals(siteMetadataList,restController.getSiteMetadataPoints());

    }

}
