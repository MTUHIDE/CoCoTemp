package space.hideaway.util.ControllersTests;
import org.junit.*;
import org.springframework.ui.Model;
import space.hideaway.controllers.NewDeviceController;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.services.DeviceService;
import space.hideaway.services.user.UserServiceImplementation;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


public class NewDeviceControllerTest {

    NewDeviceController newDeviceController;
    UserServiceImplementation MockUserServiceImplementation;
    DeviceService MockDeviceService;


    @Before
        public void setUp(){
            MockUserServiceImplementation = mock(UserServiceImplementation.class);
            MockDeviceService = mock(DeviceService.class);
            newDeviceController = new NewDeviceController(MockUserServiceImplementation,MockDeviceService);
    }

    @Test
        public void TestNewDevice(){
        Model MockModel = mock( Model.class);
        User MockUser = mock(User.class);
        Set<Site> siteSet = new HashSet<>();
        Site MockSite = mock(Site.class);
        siteSet.add(MockSite);

        when(MockUserServiceImplementation.getCurrentLoggedInUser()).thenReturn(MockUser);
        when(MockUser.getSiteSet()).thenReturn(siteSet);


        Assert.assertEquals("/newDevice",newDeviceController.newDevice(MockModel));

        verify(MockModel).addAttribute("sites",siteSet);
    }

    @Test
        public void TestAddDevice(){
        Device MockDevice = mock(Device.class);
        when(MockDeviceService.save(MockDevice)).thenReturn(MockDevice);

        Assert.assertEquals("redirect:/dashboard",newDeviceController.addDevice(MockDevice));
    }
}
