package space.hideaway.util;
import org.junit.*;

import space.hideaway.model.*;
import space.hideaway.model.site.Site;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class DataModelTest {


    UUID expectedId ;
    UUID expectedSiteId;
    Date expectedDate;
    double expectedTemp;
    Data data;
    Data otherData;

    @Before
        public void setUp(){
        expectedId = new UUID((long)324,(long)324);
        expectedSiteId = new UUID((long)324,(long)324);
        expectedDate = new Date();
        expectedTemp = 435.65;
        data = new Data(expectedId,expectedSiteId,expectedDate,expectedTemp);
        otherData = new Data();
    }


    @Test
        public void TestGetAndSetSite(){

        Site MockSite = mock(Site.class);

        data.setSite(MockSite);

        Assert.assertEquals(MockSite,data.getSite());
    }

    @Test
    public void TestGetAndSetId(){
        UUID expectedUUIDId = new UUID((long)324, (long)324);

        data.setId(expectedUUIDId);

        Assert.assertEquals(expectedUUIDId,data.getId());
    }

    @Test
    public void TestGetAndSetSiteId(){
        UUID expectedUUIDId = new UUID((long)324, (long)324);

        data.setSiteID(expectedUUIDId);

        Assert.assertEquals(expectedUUIDId,data.getSiteID());
    }
    @Test
    public void TestGetAndSetDateTime(){
        Date expectedDate = new Date();

        data.setDateTime(expectedDate);

        Assert.assertEquals(expectedDate,data.getDateTime());
    }

    @Test
    public void TestGetAndSetTemp(){
        double expectedDouble=45.5;
        data.setTemperature(expectedDouble);

        Assert.assertEquals(expectedDouble,data.getTemperature(),0.0);
    }


    @Test
    public void TestGetAndSetUserId(){
        int expecteduserId = 435;

        data.setUserID(expecteduserId);

        Assert.assertEquals(expecteduserId,data.getUserID());
    }


    @Test
    public void TestGetAndSetUser(){
        User MockUser = mock(User.class);

        data.setUser(MockUser);

        Assert.assertEquals(MockUser,data.getUser());
    }


    @Test
    public void TestGetAndSetDeviceId(){
        UUID expectedUUIDId = new UUID((long)324, (long)324);

        data.setDeviceID(expectedUUIDId);

        Assert.assertEquals(expectedUUIDId,data.getDeviceID());
    }
    @Test
    public void TestGetAndSetDevice(){
        Device MockDevice = mock(Device.class);

        data.setDevice(MockDevice);

        Assert.assertEquals(MockDevice,data.getDevice());
    }

    @Test
    public void TestToString(){
        UUID expectedUUIDId = new UUID((long)324, (long)324);
        UUID expectedSiteId = new UUID((long)324, (long)324);
        Date expectedDate = new Date();
        double expectedDouble = 43556.6;

        String expectedString = String.format(
                "Data: [ID: %s Site ID: %s Date: %s Temperature: %s]%n",
                expectedUUIDId.toString(),
                expectedSiteId,
                expectedDate,
                expectedDouble);

        data.setId(expectedUUIDId);
        data.setSiteID(expectedSiteId);
        data.setDateTime(expectedDate);
        data.setTemperature(expectedDouble);



        Assert.assertEquals(expectedString,data.toString());


    }
}

