package space.hideaway.util;
import org.junit.*;
import space.hideaway.model.Device;
import space.hideaway.model.Role;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.model.upload.UploadHistory;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class UserModelTest {

    User user;

    @Before
        public void setUp(){
        user = new User();
    }

    @Test
    public void TestGetAndSetId(){
        Long expectedId = (long)345;

        user.setId(expectedId);

        Assert.assertEquals(expectedId,user.getId());

    }


    @Test
    public void TestGetAndSetEmail(){
        String email = "email";

        user.setEmail(email);

        Assert.assertEquals(email,user.getEmail());

    }
    @Test
    public void TestGetAndSetUsername(){
        String username = "username";

        user.setUsername(username);

        Assert.assertEquals(username,user.getUsername());

    }

    @Test
    public void TestGetAndSetPassword(){
        String password = "Password123!";

        user.setPassword(password);

        Assert.assertEquals(password,user.getPassword());

    }
    @Test
    public void TestGetAndSetConfirmationPassword(){
        String password = "confirm";

        user.setConfirmationPassword(password);

        Assert.assertEquals(password,user.getConfirmationPassword());

    }

    @Test
    public void TestGetAndSetFirstName(){
        String FName = "name";

        user.setFirstName(FName);

        Assert.assertEquals(FName,user.getFirstName());

    }
    @Test
    public void TestGetAndSetMiddleInitial(){
        String Initial = "K";

        user.setMiddleInitial(Initial);

        Assert.assertEquals(Initial,user.getMiddleInitial());

    }

    @Test
    public void TestGetAndSetLastName(){
        String LName = "Name";

        user.setLastName(LName);

        Assert.assertEquals(LName,user.getLastName());

    }

    @Test
    public void TestGetAndSetRoleSet(){
        Set<Role>  roleSet= new HashSet<Role>();
        Role MockRole = mock(Role.class);
        roleSet.add(MockRole);
        user.setRoleSet(roleSet);

        Assert.assertEquals(roleSet,user.getRoleSet());

    }

    @Test
    public void TestGetAndSetSiteSet(){
        Set<Site>  SiteSet= new HashSet<Site>();
        Site MockSite = mock(Site.class);
        SiteSet.add(MockSite);
        user.setSiteSet(SiteSet);

        Assert.assertEquals(SiteSet,user.getSiteSet());

    }

    @Test
    public void TestGetAndSetUploadHistorySet(){
        Set<UploadHistory>  uploadHistorySet= new HashSet<UploadHistory>();
        UploadHistory MockUploadHistory = mock(UploadHistory.class);
        uploadHistorySet.add(MockUploadHistory);
        user.setUploadHistorySet(uploadHistorySet);

        Assert.assertEquals(uploadHistorySet,user.getUploadHistorySet());

    }

    @Test
    public void TestGetAndSetDeviceSet(){
        Set<Device>  deviceSet= new HashSet<Device>();
        Device MockDevice = mock(Device.class);
        deviceSet.add(MockDevice);
        user.setDeviceSet(deviceSet);

        Assert.assertEquals(deviceSet,user.getDeviceSet());

    }


}
