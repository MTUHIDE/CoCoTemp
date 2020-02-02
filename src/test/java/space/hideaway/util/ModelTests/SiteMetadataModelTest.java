package space.hideaway.util.ModelTests;
import org.junit.*;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;

import java.util.UUID;

import static org.mockito.Mockito.mock;

public class SiteMetadataModelTest {


    SiteMetadata siteMetadata;
    @Before
        public void setUp(){

        siteMetadata = new SiteMetadata();

    }

    @Test
        public void TestGetAndSetMetadataId(){
        UUID expectedUUID = new UUID((long) 435, (long)435);

        siteMetadata.setMetadataId(expectedUUID);

        Assert.assertEquals(expectedUUID, siteMetadata.getMetadataId());


    }

    @Test
    public void TestGetAndSetSiteId(){
        UUID expectedUUID = new UUID((long) 435, (long)435);

        siteMetadata.setSiteID(expectedUUID);

        Assert.assertEquals(expectedUUID, siteMetadata.getSiteID());


    }

    @Test
    public void TestGetAndSetSite(){
        Site MockSite = mock(Site.class);

        siteMetadata.setSite(MockSite);

        Assert.assertEquals(MockSite, siteMetadata.getSite());


    }

    @Test
    public void TestGetAndSetObstacleType(){
        String expectedObstacleType = "Type";

        siteMetadata.setObstacleType(expectedObstacleType);

        Assert.assertEquals(expectedObstacleType, siteMetadata.getObstacleType());


    }

    @Test
    public void TestGetAndSetMaxNightTime(){
        String expectedMaxNightTime= "Night";

        siteMetadata.setMaxNightTime(expectedMaxNightTime);

        Assert.assertEquals(expectedMaxNightTime, siteMetadata.getMaxNightTime());


    }

    @Test
    public void TestGetAndSetMinNightTime(){
        String expectedMinNightTime = "Day";

        siteMetadata.setMinNightTime(expectedMinNightTime);

        Assert.assertEquals(expectedMinNightTime, siteMetadata.getMinNightTime());


    }

    @Test
    public void TestGetAndSetEnvironMent(){
        String expectedEnvironment = "Day";

        siteMetadata.setEnvironment(expectedEnvironment);

        Assert.assertEquals(expectedEnvironment, siteMetadata.getEnvironment());


    }

    @Test
    public void TestGetAndSetPurpose(){
        String expectedPurpose = "Purpoise";

        siteMetadata.setPurpose(expectedPurpose);

        Assert.assertEquals(expectedPurpose, siteMetadata.getPurpose());


    }

    @Test
    public void TestGetAndSetHeightAboveGround(){
        int expectedHeightAboveGround = 192;

        siteMetadata.setHeightAboveGround(expectedHeightAboveGround);

        Assert.assertEquals(expectedHeightAboveGround, siteMetadata.getHeightAboveGround());


    }

    @Test
    public void TestGetAndSetHeightAboveFloor(){
        int expectedHeightAboveFloor = 435;

        siteMetadata.setHeightAboveFloor(expectedHeightAboveFloor);

        Assert.assertEquals(expectedHeightAboveFloor, siteMetadata.getHeightAboveFloor());

    }

    @Test
    public void TestGetAndSetEnclosurePercentage(){
        int expectedEnclosurePercentage = 2678657;

        siteMetadata.setEnclosurePercentage(expectedEnclosurePercentage);

        Assert.assertEquals(expectedEnclosurePercentage, siteMetadata.getEnclosurePercentage());

    }

    @Test
    public void TestGetAndSetNearestAirflowObstacle(){
        int expectedNearestAirflowObstacle =46;

        siteMetadata.setNearestAirflowObstacle(expectedNearestAirflowObstacle);

        Assert.assertEquals(expectedNearestAirflowObstacle, siteMetadata.getNearestAirflowObstacle());

    }
    @Test
    public void TestGetAndSetNearestObstacleDegrees(){
        int expectedNearestObstacleDegrees = 84;

        siteMetadata.setNearestObstacleDegrees(expectedNearestObstacleDegrees);

        Assert.assertEquals(expectedNearestObstacleDegrees, siteMetadata.getNearestObstacleDegrees());

    }

    @Test
    public void TestGetAndSetAreaAroundSensor(){
        int expectedAreaAroundSensor = 92;

        siteMetadata.setAreaAroundSensor(expectedAreaAroundSensor);

        Assert.assertEquals(expectedAreaAroundSensor, siteMetadata.getAreaAroundSensor());

    }
    @Test
    public void TestGetAndSetIsRiparianArea(){
        boolean expectedIsRiparianArea = true;

        siteMetadata.setRiparianArea(expectedIsRiparianArea);

        Assert.assertEquals(expectedIsRiparianArea, siteMetadata.isRiparianArea());

    }

    @Test
    public void TestGetAndSetCanopyType(){
        String expectedCanopyType = "tree";

        siteMetadata.setCanopyType(expectedCanopyType);

        Assert.assertEquals(expectedCanopyType, siteMetadata.getCanopyType());

    }

    @Test
    public void TestGetAndSetSlopeDirection(){
        int expectedSlopeDirection = 65;

        siteMetadata.setSlopeDirection(expectedSlopeDirection);

        Assert.assertEquals(expectedSlopeDirection, siteMetadata.getSlopeDirection());

    }

    @Test
    public void TestGetAndSetWaterDistance(){
        int expectedWaterDistance = 21;

        siteMetadata.setWaterDistance(expectedWaterDistance);

        Assert.assertEquals(expectedWaterDistance, siteMetadata.getWaterDistance());

    }

    @Test
    public void TestGetAndSetWaterDirection(){
        int expectedWaterDirection = 34;

        siteMetadata.setWaterDirection(expectedWaterDirection);

        Assert.assertEquals(expectedWaterDirection, siteMetadata.getWaterDirection());

    }

    @Test
    public void TestGetAndSetNearestWater(){
        String expectedNearestWater = "Salt Lake";

        siteMetadata.setNearestWater(expectedNearestWater);

        Assert.assertEquals(expectedNearestWater, siteMetadata.getNearestWater());

    }

    @Test
    public void TestGetAndSetSlope(){
        double expectedSlope = 24.5;

        siteMetadata.setSlope(expectedSlope);

        Assert.assertEquals(expectedSlope, siteMetadata.getSlope(),0.0);

    }

    @Test
    public void TestGetAndSkyViewfactor(){
        double expectedSkyViewfactor = 27.5;

        siteMetadata.setSkyViewFactor(expectedSkyViewfactor);

        Assert.assertEquals(expectedSkyViewfactor, siteMetadata.getSkyViewFactor(),0.0);

    }










}
