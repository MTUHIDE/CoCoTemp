package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.model.Device;
import space.hideaway.services.DeviceServiceImplementation;

/**
 * Created by dough on 10/28/2016.
 */
@Controller
public class DeviceController {

    @Autowired
    DeviceServiceImplementation deviceServiceImplementation;

    @RequestMapping(value = "/manage/devices/add", method = RequestMethod.POST)
    public
    @ResponseBody
    String addDevice(@RequestParam("deviceName") String deviceName,
                     @RequestParam("deviceLocation") String deviceLocation) {
        return deviceServiceImplementation.save(new Device(deviceName, deviceLocation));
    }

}
