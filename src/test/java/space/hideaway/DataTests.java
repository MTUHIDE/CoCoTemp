package space.hideaway;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;

import java.util.ArrayList;

/**
 * Created by dough on 11/1/2016.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class DataTests {

    Logger logger = Logger.getLogger(getClass());

    @Autowired
    UserServiceImplementation userServiceImplementation;

    private User testUser;
    private User largeTestUser;

    @Before
    public void setUp() throws Exception, UserNotFoundException {
        this.testUser = userServiceImplementation.findByUsername("test");
        this.largeTestUser = userServiceImplementation.findByUsername("test_large");

    }

    @Test
    public void correctTestGetDevice() throws Exception {
        ArrayList<Device> devices = new ArrayList<>(testUser.getDeviceSet());
        ArrayList<Data> datas = new ArrayList<>();

        //Since this is the small test user, there should only be one device.
        datas.addAll(devices.get(0).getDataSet());

        for (Data data : datas) {
            logger.log(Level.INFO, data);
        }

        /*
         * The device's dataSet() method should only contain one data point. That data point's getDevice() method should
         * return the same device.
         */
        datas.stream().filter(data -> !data.getDevice().equals(devices.get(0))).forEach(data -> {
            Assert.fail("The device as seen by the data point should equal the device the data point resides inside.");
        });
    }

    @Test
    public void correctLargeTestGetDevice() throws Exception {

    }
}
