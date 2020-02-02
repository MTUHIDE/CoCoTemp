package space.hideaway.util.ServicesTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.model.User;
import space.hideaway.services.data.DataServiceImplementation;
import space.hideaway.services.site.SiteServiceImplementation;
import space.hideaway.services.upload.UploadHistoryService;
import space.hideaway.services.upload.UploadService;
import space.hideaway.services.user.UserServiceImplementation;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class UploadServiceTest {
    @InjectMocks
    UploadService uploadService;

    @Mock
    Thread fileUploadThread= mock(Thread.class);

    @Mock
    FileOutputStream fileOutputStream = mock(FileOutputStream.class);

    UploadHistoryService mockUploadHistoryServiceImplementation;
    UserServiceImplementation mockUserServiceImplementation;
    SiteServiceImplementation mockSiteServiceImplementation;
    DataServiceImplementation mockDataServiceImplementation;
    @Before
        public void setUp(){
        mockUploadHistoryServiceImplementation = mock(UploadHistoryService.class);
        mockUserServiceImplementation = mock(UserServiceImplementation.class);
        mockSiteServiceImplementation = mock(SiteServiceImplementation.class);
        mockDataServiceImplementation = mock(DataServiceImplementation.class);
        uploadService = new UploadService(mockDataServiceImplementation,mockUploadHistoryServiceImplementation,mockSiteServiceImplementation,mockUserServiceImplementation);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstrutor(){
        Assert.assertNotNull(uploadService);
    }

    @Test
    public void testSetAndGetMultipartFile(){
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        uploadService.setMultipartFile(mockMultipartFile);

        Assert.assertEquals(mockMultipartFile,uploadService.getMultipartFile());
    }

    @Test
    public void testParseFileIsCorrectUserAndFileIsNotEmpty() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        User mockUser = mock(User.class);
        String description = "desc";
        String siteKey ="key";
        byte [] bytes = {100,97,116,101,84,105,109,101,84,105,109,101,44,116,101,109,112,95,115,116,97,110,100,97,114,100
                ,44,116,101,109,112,101,114,97,116,117,114,101,13,10,50,48,49,57,45,49,48,45,50,55,32,49,49,58,53,57,58,
                48,48,44,67,44,50,51,13,10,13,10};

        when(multipartFile.getBytes()).thenReturn(bytes);
        when(mockUserServiceImplementation.getCurrentLoggedInUser()).thenReturn(mockUser);
        when(mockSiteServiceImplementation.isCorrectUser(mockUser,siteKey)).thenReturn(true);
        doNothing().when(fileUploadThread).start();
        uploadService.setMultipartFile(multipartFile);
        fileUploadThread.stop();
        Assert.assertEquals("{status: \"in progress\"}",uploadService.parseFile(siteKey,description));

    }

    @Test
    public void testParseFileIsNotCorrectUserAndFileIsNotEmpty() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        User mockUser = mock(User.class);
        String description = "desc";
        String siteKey ="key";
        byte [] bytes = {100,97,116,101,84,105,109,101,84,105,109,101,44,116,101,109,112,95,115,116,97,110,100,97,114,100
                ,44,116,101,109,112,101,114,97,116,117,114,101,13,10,50,48,49,57,45,49,48,45,50,55,32,49,49,58,53,57,58,
                48,48,44,67,44,50,51,13,10,13,10};

        when(multipartFile.getBytes()).thenReturn(bytes);
        when(mockUserServiceImplementation.getCurrentLoggedInUser()).thenReturn(mockUser);
        when(mockSiteServiceImplementation.isCorrectUser(mockUser,siteKey)).thenReturn(false);
        uploadService.setMultipartFile(multipartFile);
        Assert.assertEquals("{status: \"failed\", message: \"You do not authorized to edit this site\"}",uploadService.parseFile(siteKey,description));

    }

    @Test
    public void testParseFileAndFileIsEmpty() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        User mockUser = mock(User.class);
        String description = "desc";
        String siteKey ="key";
        byte [] bytes = {};

        when(multipartFile.getBytes()).thenReturn(bytes);
        when(mockUserServiceImplementation.getCurrentLoggedInUser()).thenReturn(mockUser);
        when(mockSiteServiceImplementation.isCorrectUser(mockUser,siteKey)).thenReturn(false);
        uploadService.setMultipartFile(multipartFile);
        Assert.assertEquals("",uploadService.parseFile(siteKey,description));

    }





}
