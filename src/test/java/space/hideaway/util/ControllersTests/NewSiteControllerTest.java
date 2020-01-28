package space.hideaway.util.ControllersTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import space.hideaway.controllers.site.NewSiteController;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.services.site.SiteMetadataService;
import space.hideaway.services.site.SiteService;
import space.hideaway.validation.SiteValidator;

import java.util.UUID;

import static org.mockito.Mockito.*;


public class NewSiteControllerTest {

   private NewSiteController newSiteController;
   private SiteValidator MockSiteValidator;
   private SiteService MockSiteService;
   private SiteMetadataService MockSiteMetadataService;
   private NewSiteController spyNewSiteController;

    @Before
        public void setUp(){

        MockSiteValidator = mock(SiteValidator.class);
        MockSiteService = mock(SiteService.class);
        MockSiteMetadataService = mock(SiteMetadataService.class);
        newSiteController = new NewSiteController(MockSiteValidator,MockSiteService,MockSiteMetadataService);
        spyNewSiteController = spy(newSiteController);
    }



    @Test
        public void TestInitalPage(){
        ModelMap MockModelMap = mock(ModelMap.class);

        Assert.assertEquals("new-site/new-site-form",newSiteController.initialPage(MockModelMap));
    }

    @Test
        public void TestQuestionPageNoErrors(){
        Site MockSite = mock(Site.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockSiteValidator).validate(MockSite,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(false);
        Assert.assertEquals("new-site/site-questionnaire",newSiteController.questionPage(MockSite,MockBindingResult));
        verify(MockSiteValidator).validate(MockSite,MockBindingResult);

    }
    @Test
    public void TestQuestionPageErrors(){
        Site MockSite = mock(Site.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockSiteValidator).validate(MockSite,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(true);
        Assert.assertEquals("new-site/new-site-form",newSiteController.questionPage(MockSite,MockBindingResult));
        verify(MockSiteValidator).validate(MockSite,MockBindingResult);

    }

    @Test
    public void TestGlobePageNoErrors(){
        Site MockSite = mock(Site.class);
        SiteMetadata MockSiteMetadata = mock(SiteMetadata.class);
        Model MockModel = mock(Model.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockSiteValidator).validateDescription(MockSite,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(false);

        Assert.assertEquals("new-site/globe-questionnaire",spyNewSiteController.globePage(MockSite,MockBindingResult,MockModel));
        verify(MockModel).addAttribute("environments",spyNewSiteController.getAllEnvironments());
        verify(MockModel).addAttribute("purposes",spyNewSiteController.getAllPurposes());
        verify(MockModel).addAttribute("obstacles",spyNewSiteController.getAllObstacles());
        verify(MockModel).addAttribute("times",spyNewSiteController.getAllTimes());
        verify(MockModel).addAttribute("canopyTypes",spyNewSiteController.getAllCanopyTypes());
        verify(MockModel).addAttribute("nearestWaterTypes",spyNewSiteController.getAllWaterTypes());
        verify(MockSiteValidator).validateDescription(MockSite,MockBindingResult);

    }
    @Test
    public void TestGlobePageErrors(){
        Site MockSite = mock(Site.class);
        Model MockModel = mock(Model.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockSiteValidator).validateDescription(MockSite,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(true);
        Assert.assertEquals("new-site/site-questionnaire",newSiteController.globePage(MockSite,MockBindingResult,MockModel));
        verify(MockSiteValidator).validateDescription(MockSite,MockBindingResult);

    }

    @Test
        public void TestCreateSite(){
        Site MockSite= mock(Site.class);
        SessionStatus MockSessionStatus = mock(SessionStatus.class);
        when(MockSiteService.save(MockSite)).thenReturn(MockSite);
        doNothing().when(MockSessionStatus).setComplete();
        Assert.assertEquals("redirect:/dashboard",newSiteController.createSite(MockSite,MockSessionStatus));
    }

    @Test
        public void TestCreateGlobeSite(){
        Site MockSite = mock(Site.class);
        long expectedLong = (long)435 ;
        UUID expectedUUID = new UUID(expectedLong,expectedLong);
        SiteMetadata MockSiteMetaData = mock(SiteMetadata.class);
        SessionStatus MockSessionStatus = mock(SessionStatus.class);
        RedirectAttributes MockRedirectAttributes = mock(RedirectAttributes.class);
        when(MockSiteService.save(MockSite)).thenReturn(MockSite);
        when(MockSite.getId()).thenReturn(expectedUUID);
        doNothing().when(MockSiteMetaData).setSiteID(expectedUUID);
        when(MockSiteMetadataService.save(MockSiteMetaData)).thenReturn(MockSiteMetaData);
        when(MockSessionStatus.isComplete()).thenReturn(true);
        Assert.assertEquals("redirect:/dashboard",newSiteController.createGlobeSite(MockSite,MockSiteMetaData,MockSessionStatus,MockRedirectAttributes));
        verify(MockSiteService).save(MockSite);
        verify(MockSiteMetaData).setSiteID(expectedUUID);
        verify(MockSiteMetadataService).save(MockSiteMetaData);
        verify(MockSessionStatus).setComplete();
    }
    @Test
        public void TestGetAllTimes(){
        String [] expectedTimes =  new String[] {
                "00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
                "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
        };

        Assert.assertArrayEquals(expectedTimes,newSiteController.getAllTimes());
    }
    @Test
    public void TestGetAllPurposes(){
        String [] expectedPurposes =  new String[] {
                "Commercial Offices", "Retail", "Restaurant", "Industrial",
                "Construction Site", "Single Family Residential", "Multi Family Residential",
                "Park or Greenbelt", "Sports Facility", "Recreational Pool", "Promenade or Plaza",
                "Bike or Walking Path", "Roadway or Parking Lot"
        };

        Assert.assertArrayEquals(expectedPurposes,newSiteController.getAllPurposes());
    }
    @Test
    public void TestGetAllCanopyTypes(){
        String [] expectedCanopyTypes = new String[] {
                "No Canopy", "Tree/Vegetation", "Shade Sail", "Pergola/Ramada", "Other Solid Roof"};


                Assert.assertArrayEquals(expectedCanopyTypes,newSiteController.getAllCanopyTypes());
    }
    @Test
    public void TestGetAllWaterTypes(){
        String [] expectedWaterTypes = new String[] {
                "Swimming Pool", "Large river", "Small stream", "Lake/Pond", "Other (describe)"};


        Assert.assertArrayEquals(expectedWaterTypes,newSiteController.getAllWaterTypes());
    }
    @Test
    public void TestGetAllObstacles(){
        String [] expectedObstacles = new String[] {
                "Building", "Wall", "Hedgerow", "Other"};


        Assert.assertArrayEquals(expectedObstacles,newSiteController.getAllObstacles());
    }
    @Test
    public void TestGetAllEnvironments(){
        String [] expectedEnvironments= new String[] {
                "Natural", "Urban"};


        Assert.assertArrayEquals(expectedEnvironments,newSiteController.getAllEnvironments());
    }
}
