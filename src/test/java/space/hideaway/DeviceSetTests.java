package space.hideaway;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by dough on 11/1/2016.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class DeviceSetTests {

    @Autowired
    UserServiceImplementation userServiceImplementation;

    Logger logger = Logger.getLogger(getClass());
    User testUser;

    @Before
    public void setUp() throws Exception, UserNotFoundException {
        this.testUser = userServiceImplementation.findByUsername("test");
    }

    @After
    public void tearDown() throws Exception {


    }

    @Test
    public void deviceMapping() {
        Set<Device> deviceSet = testUser.getDeviceSet();
        for (Device device : deviceSet) {
            logger.log(Level.INFO,
                    String.format("Device: [Name: %s Location: %s UUID: %s]%n",
                            device.getDeviceName(),
                            device.getDeviceLocation(),
                            device.getDeviceUUID()));
        }
        Object[] devices = deviceSet.toArray();
        assertTrue("The only device should be the test device.", ((Device) devices[0]).getId() == 27);
    }

    @Test
    public void correctSizeDeviceSetForTestUser() throws Exception {
        Set<Device> deviceSet = testUser.getDeviceSet();
        assertTrue("The test device set should be of size 1.", deviceSet.size() == 1);
    }
}
