package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Device;
import space.hideaway.services.DeviceServiceImplementation;
import space.hideaway.services.UploadHistoryService;

import java.util.UUID;

/**
 * HIDE CoCoTemp 2016
 * Class responsible for routing requests relating to CRUD operations on devices.
 *
 * @author Piper Dougherty
 */
@Controller
public class DeviceController {

    private final
    DeviceServiceImplementation deviceServiceImplementation;
    private final UploadHistoryService uploadHistoryService;

    @Autowired
    public DeviceController(UploadHistoryService uploadHistoryService, DeviceServiceImplementation deviceServiceImplementation) {
        this.uploadHistoryService = uploadHistoryService;
        this.deviceServiceImplementation = deviceServiceImplementation;
    }


    @RequestMapping(value = "/device/{deviceID}")
    public String showDevice(Model model, @PathVariable(value = "deviceID") UUID deviceID) {
        Device device = deviceServiceImplementation.findByKey(deviceID.toString());
        model.addAttribute("device", device);
        model.addAttribute("deviceID", deviceID.toString());
        model.addAttribute("deviceServiceImplementation", deviceServiceImplementation);
        return "station";
    }

    /**
     * Add a new device to a user's device collection. Returns a JSON tree representing the status
     * of the device creation.
     * <p>
     * Example JSON of successful addition.
     * {
     * "error": false
     * }
     * <p>
     * Example JSON of unsuccessful addition.
     * {
     * "error": true,
     * "errors": ["A description of some error one.", "A description of some error two."]
     * }
     *
     * @param deviceName      The name of the new device.
     * @param deviceLatitude The latitude location of the new device.
     * @param deviceLongitude The longitude location of the new device.
     * @return A JSON representation of the status.
     */
    @RequestMapping(value = "/manage/devices/add", method = RequestMethod.POST)
    public
    @ResponseBody
    String addDevice(@RequestParam("deviceName") String deviceName,
                     @RequestParam("deviceLatitude") double deviceLatitude,
                     @RequestParam("deviceLongitude") double deviceLongitude) {
        return deviceServiceImplementation.save(new Device(deviceName, deviceLatitude, deviceLongitude));
    }

}
