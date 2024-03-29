package space.hideaway.util.ModelTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.model.upload.UploadHistory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class DeviceModelTest {

    Device device;

    @Before
        public void setUp(){

        device = new Device();
    }


    @Test
    public void TestGetAndSetId(){
        UUID expectedUUIDId = new UUID((long)324, (long)324);

        device.setId(expectedUUIDId);

        Assert.assertEquals(expectedUUIDId,device.getId());
    }

    @Test
    public void TestGetAndSetManufactureNum(){
        String expectedNum ="34234";

        device.setManufacture_num(expectedNum);

        Assert.assertEquals(expectedNum,device.getManufacture_num());
    }

    @Test
    public void TestGetAndSetTypr(){
        String expectedType ="type";

        device.setDeviceType(expectedType);

        Assert.assertEquals(expectedType,device.getDeviceType());
    }

    @Test
    public void TestGetAndSetUserId(){
        Long expectedId = (long)324324;

        device.setUserID(expectedId);

        Assert.assertEquals(expectedId,device.getUserID());
    }

    @Test
    public void TestGetAndSetSiteId(){
        UUID expectedUUIDId = new UUID((long)324, (long)324);

        device.setSiteID(expectedUUIDId);

        Assert.assertEquals(expectedUUIDId,device.getSiteID());
    }

    @Test
    public void TestGetAndSetUser(){
        User MockUser = mock(User.class);

        device.setUser(MockUser);

        Assert.assertEquals(MockUser,device.getUser());
    }

    @Test
    public void TestGetAndSetDataSet(){
        Data MockData = mock(Data.class);
        Set<Data> dataSet = new HashSet<Data>();
        dataSet.add(MockData);

        device.setDataSet(dataSet);

        Assert.assertEquals(dataSet,device.getDataSet());
    }
    @Test
    public void TestGetAndSetShelterType(){
        String expectedText = "Improvised";
        device.setShelterType(expectedText);

        Assert.assertEquals(expectedText,device.getShelterType());
    }

    @Test
    public void TestGetAndSetUploadHistories(){
        UploadHistory MockUploadHistory = mock(UploadHistory.class);
        Set<UploadHistory> uploadHistorySet = new HashSet<UploadHistory>();
        uploadHistorySet.add(MockUploadHistory);

        device.setUploadHistories(uploadHistorySet);

        Assert.assertEquals(uploadHistorySet,device.getUploadHistories());
    }

    @Test
    public void TestGetAndSetSet(){
        Site MockSite = mock(Site.class);

        device.setSite(MockSite);

        Assert.assertEquals(MockSite,device.getSite());
    }
}
