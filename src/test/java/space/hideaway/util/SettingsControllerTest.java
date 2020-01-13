package space.hideaway.util;
import org.junit.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import space.hideaway.controllers.settings.SettingsController;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.services.DeviceService;
import space.hideaway.services.site.SiteService;
import space.hideaway.services.user.UserServiceImplementation;
import space.hideaway.validation.PersonalDetailsValidator;
import space.hideaway.validation.SiteValidator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class SettingsControllerTest {

    UserServiceImplementation MockuserManagement;

     DeviceService MockDeviceService;
     SiteService MockSiteService;

     SiteValidator MockSiteValidator;
     PersonalDetailsValidator MockPersonalDetailsValidator;

     SettingsController settingsController;

     @Before

        public void setUp(){
         MockuserManagement = mock(UserServiceImplementation.class);
         MockDeviceService = mock(DeviceService.class);
         MockSiteService = mock(SiteService.class);
         MockSiteValidator = mock(SiteValidator.class);
         MockPersonalDetailsValidator = mock(PersonalDetailsValidator.class);

         settingsController = new SettingsController(MockuserManagement,MockSiteService,MockSiteValidator,MockPersonalDetailsValidator,MockDeviceService);
     }

    @Test
        public void TestShowSettings(){
         Model MockModel = mock(Model.class);
         User MockUser = mock(User.class);
        Set<Site> siteSet= new HashSet<Site>();
        Set<Device> deviceSet= new HashSet<Device>();
        Site MockSite = mock(Site.class);
        Device MockDevice = mock(Device.class);

        siteSet.add(MockSite);
        deviceSet.add(MockDevice);

        when(MockUser.getSiteSet()).thenReturn(siteSet);
        when(MockUser.getDeviceSet()).thenReturn(deviceSet);
        when(MockuserManagement.getCurrentLoggedInUser()).thenReturn(MockUser);

        Assert.assertEquals("settings/general",settingsController.showSettings(MockModel));

        verify(MockModel).addAttribute("user",MockUser);
        verify(MockModel).addAttribute("sites",siteSet);
        verify(MockModel).addAttribute("devices",deviceSet);

    }

    @Test
        public void TestUpdateGeneralNoError(){
         Model MockModel = mock(Model.class);
         User MockUser = mock(User.class);
         BindingResult MockBindingResult = mock(BindingResult.class);

         doNothing().when(MockPersonalDetailsValidator).validate(MockUser,MockBindingResult);
         doNothing().when(MockuserManagement).update(MockUser);

         Assert.assertEquals("redirect:/settings",settingsController.updateGeneral(MockModel,MockUser,MockBindingResult));

         verify(MockPersonalDetailsValidator).validate(MockUser,MockBindingResult);
         verify(MockuserManagement).update(MockUser);

    }
    @Test
    public void TestUpdateGeneralErrors(){
        Model MockModel = mock(Model.class);
        User MockUser = mock(User.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        Set<Site> siteSet= new HashSet<Site>();
        Set<Device> deviceSet= new HashSet<Device>();
        Site MockSite = mock(Site.class);
        Device MockDevice = mock(Device.class);
        siteSet.add(MockSite);
        deviceSet.add(MockDevice);

        when(MockUser.getSiteSet()).thenReturn(siteSet);
        when(MockUser.getDeviceSet()).thenReturn(deviceSet);
        when(MockuserManagement.getCurrentLoggedInUser()).thenReturn(MockUser);
        doNothing().when(MockPersonalDetailsValidator).validate(MockUser,MockBindingResult);
        doNothing().when(MockuserManagement).update(MockUser);
        when(MockBindingResult.hasErrors()).thenReturn(true);
        Assert.assertEquals("settings/general",settingsController.updateGeneral(MockModel,MockUser,MockBindingResult));

        verify(MockPersonalDetailsValidator).validate(MockUser,MockBindingResult);
    }

    @Test
        public void TestLoadSiteCorrectUser(){
         Model MockModel = mock(Model.class);
         UUID expectedUUID = new UUID((long)435,(long)435);
         String UUIDString = expectedUUID.toString();
         User MockUser = mock(User.class);
        Set<Site> siteSet= new HashSet<Site>();
        Set<Device> deviceSet= new HashSet<Device>();
        Site MockSite = mock(Site.class);
        Device MockDevice = mock(Device.class);
        siteSet.add(MockSite);
        deviceSet.add(MockDevice);

        when(MockuserManagement.getCurrentLoggedInUser()).thenReturn(MockUser);
         when(MockSiteService.isCorrectUser(MockUser,UUIDString)).thenReturn(true);
         when(MockModel.containsAttribute("site")).thenReturn(false);
         when(MockSiteService.findByKey(UUIDString)).thenReturn(MockSite);
         when(MockUser.getSiteSet()).thenReturn(siteSet);
         when(MockUser.getDeviceSet()).thenReturn(deviceSet);
         when(MockSite.getDeviceSet()).thenReturn(deviceSet);

         Assert.assertEquals("settings/site",settingsController.loadSite(MockModel,expectedUUID));

         verify(MockModel).addAttribute("site",MockSite);
         verify(MockModel).addAttribute("sites",siteSet);
         verify(MockModel).addAttribute("devices",deviceSet);
         verify(MockModel).addAttribute("siteDevices",deviceSet);
    }

    @Test
        public void TestLoadSiteWrongUser(){
        Model MockModel = mock(Model.class);
        UUID expectedUUID = new UUID((long)435,(long)435);
        String UUIDString = expectedUUID.toString();
        User MockUser = mock(User.class);

        when(MockuserManagement.getCurrentLoggedInUser()).thenReturn(MockUser);
        when(MockSiteService.isCorrectUser(MockUser,UUIDString)).thenReturn(false);

        Assert.assertEquals("error/no-access",settingsController.loadSite(MockModel,expectedUUID));

    }

    @Test
    public void TestUpdateSiteNoError(){
        Site MockSite = mock(Site.class);
        UUID expectedUUID = new UUID((long)435,(long)435);

        BindingResult MockBindingResult = mock(BindingResult.class);
        RedirectAttributes MockRedirectAttributes = mock(RedirectAttributes.class);
        String expectedString =  "redirect:/settings/site?siteID=" + expectedUUID.toString();
        doNothing().when(MockSiteValidator).validate(MockSite,MockBindingResult);
        doNothing().when(MockSiteValidator).validateDescription(MockSite,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(false);
        when(MockSiteService.save(MockSite)).thenReturn(MockSite);
        when(MockSite.getId()).thenReturn(expectedUUID);
        Assert.assertEquals(expectedString,settingsController.updateSite(MockSite,MockBindingResult,MockRedirectAttributes));

    }
    @Test
    public void TestUpdateSiteErrors(){
        Site MockSite = mock(Site.class);
        UUID expectedUUID = new UUID((long)435,(long)435);

        BindingResult MockBindingResult = mock(BindingResult.class);
        RedirectAttributes MockRedirectAttributes = mock(RedirectAttributes.class);

        String expectedString =  "redirect:/settings/site?siteID=" + expectedUUID.toString();
        doNothing().when(MockSiteValidator).validate(MockSite,MockBindingResult);
        doNothing().when(MockSiteValidator).validateDescription(MockSite,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(true);
        when(MockRedirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.site",MockBindingResult)).thenReturn(MockRedirectAttributes);
        when(MockRedirectAttributes.addFlashAttribute("site",MockSite)).thenReturn(MockRedirectAttributes);
        when(MockSite.getId()).thenReturn(expectedUUID);
        Assert.assertEquals(expectedString,settingsController.updateSite(MockSite,MockBindingResult,MockRedirectAttributes));

    }
    @Test
        public void TestLoadDeviceCorrectUser(){

        Model MockModel = mock(Model.class);
        UUID expectedUUID = new UUID((long)435,(long)435);
        String UUIDString = expectedUUID.toString();
        User MockUser = mock(User.class);
        Set<Site> siteSet= new HashSet<Site>();
        Set<Device> deviceSet= new HashSet<Device>();
        Site MockSite = mock(Site.class);
        Device MockDevice = mock(Device.class);
        siteSet.add(MockSite);
        deviceSet.add(MockDevice);

        when(MockuserManagement.getCurrentLoggedInUser()).thenReturn(MockUser);
        when(MockDeviceService.isCorrectUser(MockUser,UUIDString)).thenReturn(true);
        when(MockModel.containsAttribute("device")).thenReturn(false);
        when(MockDeviceService.findByKey(UUIDString)).thenReturn(MockDevice);
        when(MockUser.getSiteSet()).thenReturn(siteSet);
        when(MockUser.getDeviceSet()).thenReturn(deviceSet);
        when(MockSite.getDeviceSet()).thenReturn(deviceSet);

        Assert.assertEquals("settings/device",settingsController.loadDevice(MockModel,expectedUUID));

        verify(MockModel).addAttribute("device",MockDevice);
        verify(MockModel).addAttribute("sites",siteSet);
        verify(MockModel).addAttribute("devices",deviceSet);

    }

    @Test
    public void TestLoadDeviceWrongUser(){
        Model MockModel = mock(Model.class);
        UUID expectedUUID = new UUID((long)435,(long)435);
        String UUIDString = expectedUUID.toString();
        User MockUser = mock(User.class);

        when(MockuserManagement.getCurrentLoggedInUser()).thenReturn(MockUser);
        when(MockDeviceService.isCorrectUser(MockUser,UUIDString)).thenReturn(false);

        Assert.assertEquals("error/no-access",settingsController.loadSite(MockModel,expectedUUID));

    }

    @Test
        public void TestUpdateDevice(){
        Device MockDevice = mock(Device.class);
        UUID expectedUUID = new UUID((long)435,(long)435);
        String expectedString = "redirect:/settings/device?deviceID=" + expectedUUID.toString();
        when(MockDeviceService.save(MockDevice)).thenReturn(MockDevice);
        when(MockDevice.getId()).thenReturn(expectedUUID);

        Assert.assertEquals(expectedString,settingsController.updateDevice(MockDevice));

    }

    @Test
        public void TestDeleteDevice(){

        Device MockDevice = mock(Device.class);
        doNothing().when(MockDeviceService).delete(MockDevice);

        Assert.assertEquals("redirect:/settings/",settingsController.deleteDevice(MockDevice));

    }


}
