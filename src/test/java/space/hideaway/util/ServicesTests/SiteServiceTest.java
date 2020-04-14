package space.hideaway.util.ServicesTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.repositories.UserRepository;
import space.hideaway.repositories.site.SiteRepository;
import space.hideaway.services.site.SiteService;
import space.hideaway.services.site.SiteServiceImplementation;
import space.hideaway.services.user.UserService;

import java.util.*;

import static org.mockito.Mockito.*;

public class SiteServiceTest {
    SiteService siteService;
    UserService mockUserService;
    SiteRepository mockSiteRepository;
    UserRepository mockuserRepository;

    @Before
    public void setUp(){
    mockUserService = mock(UserService.class);
    mockSiteRepository = mock(SiteRepository.class);
    mockuserRepository = mock(UserRepository.class);
    siteService = new SiteServiceImplementation(mockUserService,mockSiteRepository,mockuserRepository);
    }

    @Test
    public void testSave(){
        Site mockSite = mock(Site.class);
        User mockUser = mock(User.class);
        Long expectedLong = new Long(21);
        when(mockUserService.getCurrentLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(expectedLong);
        doNothing().when(mockSite).setUserID(expectedLong);
        when(mockSiteRepository.save(mockSite)).thenReturn(mockSite);

        Site actualSite = siteService.save(mockSite);

        verify(mockSiteRepository,times(1)).save(mockSite);
        Assert.assertEquals(mockSite,actualSite);
    }

    @Test
    public void testFindByKey(){
        String expectedSiteId = "0e927f83-8a1e-48f5-ac3d-fb86b00dacd4";
        Site mockSite = mock(Site.class);
        UUID expectedUUID = UUID.fromString(expectedSiteId);
        when(mockSiteRepository.findById(expectedUUID)).thenReturn(Optional.ofNullable(mockSite));

        Site actualSite = siteService.findByKey(expectedSiteId);
        Assert.assertEquals(mockSite,actualSite);
    }

    @Test
    public void testGetAllSites(){
        List<Site> siteList = new ArrayList<Site>();
        Site mockSite = mock(Site.class);
        siteList.add(mockSite);

        when(mockSiteRepository.findAll()).thenReturn(siteList);

        List<Site> actualList = siteService.getAllSites();

        Assert.assertEquals(siteList,actualList);

    }

    @Test
    public void testIsCorrectUserNullCase(){

        String fakeId = "Id";
        Assert.assertFalse(siteService.isCorrectUser(null,fakeId));

    }

    @Test
    public void testIsCorrectUserNotNullCase(){
        Site site = new Site();
        String UUIDString ="0e927f83-8a1e-48f5-ac3d-fb86b00dacd4";
        User mockUser = mock(User.class);
        UUID expectedUUID = UUID.fromString(UUIDString);
        site.setId(expectedUUID);
        Set<Site> siteSet = new HashSet<Site>();
        siteSet.add(site);
        when(mockUser.getSiteSet()).thenReturn(siteSet);

        Assert.assertTrue(siteService.isCorrectUser(mockUser,UUIDString));
    }


    @Test
    public void testIsCorrectUserWithSiteKey(){
        User mockUser = mock(User.class);
        Site site = new Site();
        String UUIDString ="0e927f83-8a1e-48f5-ac3d-fb86b00dacd4";
        UUID expectedUUID = UUID.fromString(UUIDString);
        site.setId(expectedUUID);
        Set<Site> siteSet = new HashSet<Site>();
        siteSet.add(site);
        when(mockUser.getSiteSet()).thenReturn(siteSet);
        when(mockUserService.getCurrentLoggedInUser()).thenReturn(mockUser);


        Assert.assertTrue(siteService.isCorrectUser(UUIDString));
    }


    @Test
    public void testCountByUserId(){
        Long expectedLong = new Long(34);
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(expectedLong);

        when(mockSiteRepository.countByUserID(expectedLong)).thenReturn(expectedLong);

        Assert.assertEquals(expectedLong,siteService.countByUserID(mockUser));
    }
}
