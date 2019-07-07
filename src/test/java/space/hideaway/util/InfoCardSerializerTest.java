package space.hideaway.util;
import org.junit.*;
import space.hideaway.model.json.InfoCardSerializer;

public class InfoCardSerializerTest {


    InfoCardSerializer infoCardSerializer ;
    @Before
        public void setUp(){
        infoCardSerializer = new InfoCardSerializer();
    }

    @Test
        public void TestSetAndGetSiteCount(){
        Long expectedSiteCount = (long) 435;
        infoCardSerializer.setSiteCount((expectedSiteCount));

        Assert.assertEquals(expectedSiteCount,infoCardSerializer.getSiteCount());
    }
    @Test
    public void TestSetAndGetRecordCount(){
        Long expectedRecordCount = (long) 435;
        infoCardSerializer.setRecordCount((expectedRecordCount));

        Assert.assertEquals(expectedRecordCount,infoCardSerializer.getRecordCount());
    }
    @Test
    public void TestSetAndGetUploadCount(){
        Long expectedUploadCount = (long) 435;
        infoCardSerializer.setUploadCount((expectedUploadCount));

        Assert.assertEquals(expectedUploadCount,infoCardSerializer.getUploadCount());
    }
    @Test
    public void TestSetAndGetDeviceCount(){
        Long expectedDeviceCount = (long) 435;
        infoCardSerializer.setDeviceCount((expectedDeviceCount));

        Assert.assertEquals(expectedDeviceCount,infoCardSerializer.getDeviceCount());
    }
}
