package space.hideaway.util.ControllersTests;

import org.junit.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.controllers.UploadController;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.services.upload.UploadService;
import space.hideaway.services.user.UserService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class UploadControllerTest {

    UploadService MockUploadService ;

    UserService MockUserService;
    UploadController uploadController;

    @Before
        public void setUp(){
        MockUploadService = mock(UploadService.class);
        MockUserService = mock(UserService.class);
        uploadController = new UploadController(MockUploadService,MockUserService);
    }

    @Test
        public void TestShowUploadForm(){

        Model MockModel = mock(Model.class);
        User MockUser = mock(User.class);
        Set<Site> siteSet= new HashSet<Site>();
        Site MockSite = mock(Site.class);
        siteSet.add(MockSite);
        when(MockUserService.getCurrentLoggedInUser()).thenReturn(MockUser);
        when(MockUser.getSiteSet()).thenReturn(siteSet);


        Assert.assertEquals("upload",uploadController.showUploadForm(MockModel));

        verify(MockModel).addAttribute("sites",siteSet);


    }

    @Test
        public void TestUploadFiles(){
        UUID expectedId = new UUID((long)435,(long)435);
        MultipartFile MockMultipartFile = mock(MultipartFile.class);
        String expectedDescription = "description";
        String expectedIdString =  expectedId.toString();
        when(MockUploadService.setMultipartFile(MockMultipartFile)).thenReturn(MockUploadService);
        when(MockUploadService.parseFile(expectedIdString,expectedDescription)).thenReturn(expectedDescription);

        Assert.assertEquals("redirect:/dashboard",uploadController.uploadFile(expectedId,MockMultipartFile,expectedDescription));

    }
}
