package space.hideaway.util;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.*;
import space.hideaway.model.Device;
import space.hideaway.model.site.Site;
import space.hideaway.model.upload.UploadHistory;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class UploadHistoryTest {
    UploadHistory uploadHistory;

    @Before
        public void setUp(){

        uploadHistory = new UploadHistory();
    }

    @Test
        public void TestGetAndSetId(){
        UUID expectedUUID = new UUID((long )435,(long)435);

        uploadHistory.setId(expectedUUID);

        Assert.assertEquals(expectedUUID,uploadHistory.getId());


    }


    @Test
    public void TestGetAndSetSite(){
        Site MockSite = mock(Site.class);

        uploadHistory.setSite(MockSite);

        Assert.assertEquals(MockSite,uploadHistory.getSite());


    }

    @Test
    public void TestGetAndSetSiteId(){
        UUID expectedUUID = new UUID((long )435,(long)435);

        uploadHistory.setSiteID(expectedUUID);

        Assert.assertEquals(expectedUUID,uploadHistory.getSiteID());


    }
    @Test
    public void TestGetAndSetUserId(){
        int expectedId = 543;

        uploadHistory.setUserID(expectedId);

        Assert.assertEquals(expectedId,uploadHistory.getUserID());

    }

    @Test
    public void TestGetAndSetError(){
        boolean Error = true;

        uploadHistory.setError(Error);

        Assert.assertEquals(Error,uploadHistory.isError());

    }

    @Test
    public void TestGetAndSetDateTime(){
        Date expectedDate = new Date();

        uploadHistory.setDateTime(expectedDate);

        Assert.assertEquals(expectedDate,uploadHistory.getDateTime());

    }

    @Test
    public void TestGetAndSetDuration(){
        Long expectedDuration = (long) 4546;

        uploadHistory.setDuration(expectedDuration);

        Assert.assertEquals(expectedDuration,uploadHistory.getDuration());

    }

    @Test
    public void TestGetAndSetDescription(){
        String expectedDescription = "desc";

        uploadHistory.setDescription(expectedDescription);

        Assert.assertEquals(expectedDescription,uploadHistory.getDescription());

    }

    @Test
    public void TestGetAndSetRecords(){
        int expectedRecord = 654;

        uploadHistory.setRecords(expectedRecord);

        Assert.assertEquals(expectedRecord,uploadHistory.getRecords());

    }

    @Test
    public void TestGetAndSetViewed(){
        boolean viewed = true;

        uploadHistory.setViewed(viewed);

        Assert.assertEquals(viewed,uploadHistory.isViewed());

    }

    @Test
    public void TestGetAndSetDeviceId(){
        UUID expectedUUID = new UUID((long)435,(long)345345);

        uploadHistory.setDeviceID(expectedUUID);

        Assert.assertEquals(expectedUUID,uploadHistory.getDeviceID());

    }

    @Test
    public void TestGetAndSetDevice(){
        Device  MockDevice = mock(Device.class);

        uploadHistory.setDevice(MockDevice);

        Assert.assertEquals(MockDevice,uploadHistory.getDevice());

    }

}
