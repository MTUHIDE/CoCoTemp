package space.hideaway.util;


import org.junit.*;
import org.mockito.Mockito.*;
import org.springframework.ui.Model;
import space.hideaway.controllers.DashboardController;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.services.user.UserServiceImplementation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DashboardControllerTest {
    DashboardController dashboardController;
    UserServiceImplementation MockUserServiceImplementation;

    @Before
    public void setUp(){
        MockUserServiceImplementation = mock(UserServiceImplementation.class);
        dashboardController = new DashboardController(MockUserServiceImplementation);

    }

    @Test
        public void TestDashboard(){
        Model MockModel = mock(Model.class);
        User MockUser = mock(User.class);
        Site MockSite = mock(Site.class);
        Device MockDevice = mock(Device.class);
        Set<Site> siteSet = new HashSet<>();
        Set<Device> deviceSet = new HashSet<>();

        siteSet.add(MockSite);
        deviceSet.add(MockDevice);
        String expectedUsername = "CoCoTemp";

        when(MockUser.getUsername()).thenReturn(expectedUsername);
        when(MockUser.getSiteSet()).thenReturn(siteSet);
        when(MockUser.getDeviceSet()).thenReturn(deviceSet);
        when(MockUserServiceImplementation.getCurrentLoggedInUser()).thenReturn(MockUser);
        Assert.assertEquals("dashboard",dashboardController.dashboard(MockModel));

        verify(MockModel).addAttribute("greeting","Hello, "+expectedUsername);
        verify(MockModel).addAttribute("sites",siteSet);
        verify(MockModel).addAttribute("devices",deviceSet);


    }
}
