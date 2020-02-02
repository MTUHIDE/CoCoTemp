package space.hideaway.util.ControllersTests;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import space.hideaway.controllers.site.SiteMetadataController;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.repositories.site.SiteMetadataRepository;
import space.hideaway.repositories.site.SitePredicatesBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class SiteMetadataControllerTest {
    SiteMetadataRepository MockSiteMetaDataRepository;
    SiteMetadataController siteMetadataController;
    SiteMetadataController spySiteMetaDataController;

    @Before
        public void setUp(){
        MockSiteMetaDataRepository = mock(SiteMetadataRepository.class);
        siteMetadataController = new SiteMetadataController(MockSiteMetaDataRepository);
        spySiteMetaDataController = spy(siteMetadataController);
    }
//TODO Find a way to test without lame spying the CreateSiteMetaDataList Method
//    @Test
//        public void TestGetSiteMetaDataPoint(){
//        JSONObject expectedJSONObject = new JSONObject();
//        JSONArray expectedRulesArray = new JSONArray();
//        JSONObject expectedIDJSONObject = new JSONObject();
//        JSONObject expectedFieldJSONObject = new JSONObject();
//        JSONObject expectedTypeJSONObject = new JSONObject();
//        JSONObject expectedInputJSONObject = new JSONObject();
//        JSONObject expectedOperatorJSONObject = new JSONObject();
//        JSONObject expectedValueJSONObject = new JSONObject();
//
//
//        JSONObject expectedInnerRulesArray = new JSONObject();
//
//        expectedInnerRulesArray.put("id","purpose");
//        expectedInnerRulesArray.put("operator","equal");
//        expectedInnerRulesArray.put("value","Commerical Offices");
//        expectedInnerRulesArray.put("field","purpose");
//        expectedInnerRulesArray.put("type","string");
//        expectedInnerRulesArray.put("input","select");
//
//
//
//        expectedRulesArray.put(expectedInnerRulesArray);
//        expectedJSONObject.put("condition","AND");
//        expectedJSONObject.put("rules",expectedRulesArray);
//        expectedJSONObject.put("valid",true);
//
//
//        JSONArray rules = expectedJSONObject.getJSONArray("rules");
//
//        String expectedJsonString = expectedJSONObject.toString();
//
//
//
//
//        JSONObject jsonObj = new JSONObject(expectedJsonString);
//
//    }

    @Test
        public void TestCreateSiteMetaDataList(){
        SitePredicatesBuilder Mockbuilder = mock( SitePredicatesBuilder.class);
        JSONObject MockJSON = mock(JSONObject.class);

        Site MockSiteOne = mock(Site.class);
        Site MockSiteTwo = mock(Site.class);

        SiteMetadata MockSiteMetaDataOne = mock(SiteMetadata.class);
        SiteMetadata MockSiteMetaDataTwo = mock(SiteMetadata.class);

        when(MockSiteMetaDataOne.getSite()).thenReturn(MockSiteOne);
        when(MockSiteMetaDataTwo.getSite()).thenReturn(MockSiteTwo);

        SiteMetadata[] expectedMockSiteMetaDataArray = {MockSiteMetaDataOne,MockSiteMetaDataTwo};
        List<SiteMetadata> expectedSiteMetadataList =Arrays.asList(expectedMockSiteMetaDataArray);


        List expectedResultList = new ArrayList();
        Object[] expectedListObjectOne = new Object[2];
        Object[] expectedListObjectTwo = new Object[2];
        expectedListObjectOne[0] = MockSiteOne;
        expectedListObjectOne[1] = MockSiteMetaDataOne;

        expectedListObjectTwo[0] = MockSiteTwo;
        expectedListObjectTwo[1] = MockSiteMetaDataTwo;


        expectedResultList.add(expectedListObjectOne);
        expectedResultList.add(expectedListObjectTwo);
        BooleanExpression MockBool = mock(BooleanExpression.class);
        when(MockJSON.get("condition")).thenReturn("MockString");
        when(Mockbuilder.build("MockString")).thenReturn(MockBool);
        when(MockSiteMetaDataRepository.findAll(Mockito.any(BooleanExpression.class))).thenReturn(expectedSiteMetadataList);

        List actualList = siteMetadataController.CreateSiteMetaDataList(Mockbuilder,MockJSON);
        Object[] actualArray = actualList.toArray();
        Object[] expectedArray = expectedResultList.toArray();
        Assert.assertTrue(Arrays.deepEquals(expectedArray,actualArray));

    }

    @Test
        public void  TestGetShowSite(){
        Model MockModel = mock(Model.class);
        SiteMetadata MockSiteMetadata = mock(SiteMetadata.class);
        long expectedLong = (long) 3456;
        UUID expectedId = new UUID(expectedLong,expectedLong);

        when(MockSiteMetaDataRepository.findBySiteID(expectedId)).thenReturn(MockSiteMetadata);



        Assert.assertEquals(MockSiteMetadata,siteMetadataController.showSite(MockModel,expectedId));
        verify(MockSiteMetaDataRepository).findBySiteID(expectedId);


    }

}
