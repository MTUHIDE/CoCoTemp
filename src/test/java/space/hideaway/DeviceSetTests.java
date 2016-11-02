package space.hideaway;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertThat;
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

    private User testUser;
    private User largeTestUser;

    @Before
    public void setUp() throws Exception, UserNotFoundException {
        this.testUser = userServiceImplementation.findByUsername("test");
        this.largeTestUser = userServiceImplementation.findByUsername("testlarge");
    }

    @Test
    public void testDeviceMapping() {
        Set<Device> deviceSet = testUser.getDeviceSet();

        //Simply print out the devices for the small test user.
        for (Device device : deviceSet) {
            logger.log(Level.INFO, device);
        }

        ArrayList<Device> devices = new ArrayList<>(deviceSet);
        assertTrue("The only device for the small test user should be the \"Test Device\"", devices.get(0).getDeviceName().equals("Test Device"));
    }

    @Test
    public void largeTestDeviceMapping() throws Exception {
        ArrayList<String> correctDeviceNames = new ArrayList<>();
        correctDeviceNames.add("Test Device 1");
        correctDeviceNames.add("Test Device 2");
        correctDeviceNames.add("Test Device 3");
        correctDeviceNames.add("Test Device 4");
        correctDeviceNames.add("Test Device 5");

        Set<Device> deviceSet = largeTestUser.getDeviceSet();
        ArrayList<String> deviceNames = new ArrayList<>();

        //Simply print out the devices for the large test user.
        for (Device device : deviceSet) {
            logger.log(Level.INFO, device);
            deviceNames.add(device.getDeviceName());
        }
        assertThat("The database list and the supplied list should both contain the same elements.",
                correctDeviceNames,
                Matchers.containsInAnyOrder(deviceNames.toArray()));
    }

    @Test
    public void testDeviceSetSize() throws Exception {
        Set<Device> deviceSet = testUser.getDeviceSet();
        assertTrue("The test device set should be of size 1.", deviceSet.size() == 1);
    }

    @Test
    public void largeTestDeviceSetSize() throws Exception {
        Set<Device> deviceSet = largeTestUser.getDeviceSet();
        assertTrue("The large test device set should be of size 5.", deviceSet.size() == 5);
    }
}
