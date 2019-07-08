package space.hideaway.util;
import org.junit.*;
import space.hideaway.model.site.Site;
import space.hideaway.model.upload.UploadFormWrapper;

import static org.mockito.Mockito.mock;

public class UploadFormWrapperTest {

    UploadFormWrapper uploadFormWrapper;

    @Before
        public void setUp(){
        uploadFormWrapper = new UploadFormWrapper();
    }

    @Test
        public void TestGetAndSetSelectedSite(){

        Site MockSite = mock(Site.class);

        uploadFormWrapper.setSelectedSite(MockSite);

        Assert.assertEquals(MockSite,uploadFormWrapper.getSelectedSite());

    }

    @Test
        public void TestGetAndSetDescription(){
        String expectedDescription = "description";

        uploadFormWrapper.setDescription(expectedDescription);

        Assert.assertEquals(expectedDescription,uploadFormWrapper.getDescription());

    }

}
