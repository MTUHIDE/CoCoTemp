package space.hideaway;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dough on 11/1/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class DataTests {

    Logger logger = Logger.getLogger(getClass());

    @Autowired
    UserServiceImplementation userServiceImplementation;

    @Test
    public void correctDeviceMapping() throws Exception {
        User testUser = userServiceImplementation.findByUsername("test");
        Set<Device> deviceSet = testUser.getDeviceSet();
        Set<Data> allData = new HashSet<>();
        for (Device device : deviceSet) {
            allData.addAll(device.getDataSet());
        }
        for (Data data : allData) {
            logger.log(Level.INFO, String.format("Data: [ID: %s Device ID: %d Date: %s Temperature: %f]%n", data.getId(), data.getDeviceID(), data.getDateTime(), data.getTemperature()));
        }

        ArrayList<Device> devices = new ArrayList<>(deviceSet);
        ArrayList<Data> data = new ArrayList<>(allData);
        Assert.assertEquals("The device obtained from a data's getDevice() should be the same as the device it originated from.",
                devices.get(0),
                data.get(0).getDevice());
    }
}
