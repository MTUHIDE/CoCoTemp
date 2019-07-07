package space.hideaway.util;

import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import org.hibernate.search.spatial.Coordinates;
import org.junit.*;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;

import space.hideaway.model.Data;
import space.hideaway.model.upload.UploadHistory;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.mockito.Mockito.mock;


public class SiteModelTest {

    String siteName ;
    double latitude ;
    double longitude;
    double siteElevation;
    Site  site ;
    @Before
        public void setUp(){

        siteName ="site";
        siteElevation = 1.0;
        longitude = 14.5;
        latitude = 72.4;
        site = new Site(siteName,latitude,longitude,siteElevation);
    }

    @Test
        public void TestGetandSetSiteStatisticsList(){
        List<SiteStatistics> siteStatisticsList = new ArrayList<SiteStatistics>();
        SiteStatistics MockSiteStatistics = mock(SiteStatistics.class);

        siteStatisticsList.add(MockSiteStatistics);

        site.setSiteStatisticsList(siteStatisticsList);

        Assert.assertEquals(siteStatisticsList,site.getSiteStatisticsList());
    }

    @Test
        public void TestGetAndSetUserId(){

        Long expectedUserId = (long) 4356;

        site.setUserID(expectedUserId);

        Assert.assertEquals(expectedUserId,site.getUserID());
    }

    @Test
        public void TestGetAndSetUser(){
        User MockUser = mock(User.class);

        site.setUser(MockUser);

        Assert.assertEquals(MockUser,site.getUser());

    }
    @Test
        public void TestGetAndSetDataSet(){
        Set<Data> dataSet = new HashSet<Data>();
        Data MockData = mock(Data.class);

        dataSet.add(MockData);

        site.setDataSet(dataSet);

        Assert.assertEquals(dataSet,site.getDataSet());

    }
    @Test
        public void TestGetAndSetUplaodHistories(){

        Set<UploadHistory> uploadHistorySet = new HashSet<UploadHistory>();
        UploadHistory MockUploadhistory = mock(UploadHistory.class);
        uploadHistorySet.add(MockUploadhistory);

        site.setUploadHistories(uploadHistorySet);

        Assert.assertEquals(uploadHistorySet,site.getUploadHistories());

    }

    @Test
        public void TestGetAndSetId(){
        UUID expectedUUID = new UUID((long)345,(long)345);

        site.setId(expectedUUID);

        Assert.assertEquals(expectedUUID,site.getId());
    }
    @Test
        public void TestGetAndSetSiteName(){
        String expectedSiteName = "Name";

        site.setSiteName(expectedSiteName);

        Assert.assertEquals(expectedSiteName,site.getSiteName());
    }

    @Test

        public void TestGetAndSetLatitude(){
        double expectedLatitude = 14.5;

        site.setSiteLatitude(expectedLatitude);

        Assert.assertEquals(expectedLatitude,site.getSiteLatitude(),0.0);
    }

    @Test

    public void TestGetAndSetLongitude(){
        double expectedLongitude = 14.5;

        site.setSiteLongitude(expectedLongitude);

        Assert.assertEquals(expectedLongitude,site.getSiteLongitude(),0.0);
    }

    @Test
        public void TestGetAndSetSiteDescription(){
        String expectedDescription = "description";

        site.setSiteDescription(expectedDescription);

        Assert.assertEquals(expectedDescription,site.getSiteDescription());

    }
    @Test

        public void TestGetAndSetElevation(){
        double expectedElevation = 15.6;

        site.setSiteElevation(expectedElevation);

        Assert.assertEquals(expectedElevation,site.getSiteElevation(),0.0);

    }

    @Test
        public void TestGetAndSetDeviceSet(){
        Set<Device> deviceSet = new HashSet<Device>();
        Device MockDevice = mock(Device.class);
        deviceSet.add(MockDevice);

        site.setDeviceSet(deviceSet);

        Assert.assertEquals(deviceSet,site.getDeviceSet());
    }

    @Test
        public void TestGetLocation(){

        double expectedLongitude = 14.5;
        double expectgedLatitude = 20.7;
        site.setSiteLongitude(expectedLongitude);
        site.setSiteLatitude(expectgedLatitude);


        double actualLongitude = site.getLocation().getLongitude();
        double actualLatitude = site.getLocation().getLatitude();
        Assert.assertEquals(expectedLongitude,actualLongitude,0.0);
        Assert.assertEquals(expectgedLatitude,actualLatitude,0.0);

    }

    @Test
        public void TestToString(){
        String expectedSiteName = "Name";
        double expectedLongitude = 14.5;
        double expectgedLatitude = 20.7;
        UUID expectedUUID = new UUID((long)435, (long)435);
        String expectedString = String.format(   "Site: [ID: %s Name: %s Location: %s, %s]%n",
                expectedUUID,
                expectedSiteName,
                expectgedLatitude,
                expectedLongitude);

        site.setId(expectedUUID);
        site.setSiteName(expectedSiteName);
        site.setSiteLongitude(expectedLongitude);
        site.setSiteLatitude(expectgedLatitude);

        Assert.assertEquals(expectedString,site.toString());
    }


}
