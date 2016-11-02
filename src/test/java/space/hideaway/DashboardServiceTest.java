package space.hideaway;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.User;
import space.hideaway.services.DashboardServiceImplementation;
import space.hideaway.services.UserServiceImplementation;

/**
 * Created by dough on 11/1/2016.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class DashboardServiceTest {

    @Autowired
    private UserServiceImplementation userServiceImplementation;
    @Autowired
    private DashboardServiceImplementation dashboardServiceImplementation;

    private User testUser;
    private User largeTestUser;

    @Before
    public void setUp() throws UserNotFoundException {
        this.testUser = userServiceImplementation.findByUsername("test");
        this.largeTestUser = userServiceImplementation.findByUsername("test_large");
    }

    @Test
    public void getAllData() throws Exception {
        Assert.assertEquals("The size of the set of all data should equal 1.",
                dashboardServiceImplementation.getAllData(testUser).size(),
                1);
    }

    @Test
    public void getAllDataLarge() throws Exception {
        Assert.assertEquals("The size of the set of all data should equal 1000.",
                dashboardServiceImplementation.getAllData(largeTestUser).size(),
                1000);
    }

    @Test
    public void getMaxRecording() throws Exception {
        Assert.fail();
    }

    @Test
    public void getMinRecording() throws Exception {
        Assert.fail();
    }

    @Test
    public void getLastWeekAverage() throws Exception {
        Assert.fail();
    }

    @Test
    public void getLastMonthAverage() throws Exception {
        Assert.fail();
    }

    @Test
    public void getNumberOfRecords() throws Exception {
        Assert.assertEquals(dashboardServiceImplementation.getNumberOfRecords(testUser), 1);
    }

    @Test
    public void getNumberOfRecordsLarge() throws Exception {
        Assert.assertEquals(dashboardServiceImplementation.getNumberOfRecords(largeTestUser), 1000);
    }

}