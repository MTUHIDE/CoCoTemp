package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.hideaway.InfoCardSerializer;
import space.hideaway.model.Device;
import space.hideaway.model.UploadHistory;
import space.hideaway.repositories.DataRepository;
import space.hideaway.services.*;

import java.util.*;


@RestController
public class RESTController {


    /**
     * Backing service for this controller.
     */
    private final
    RESTService restService;

    /**
     * The JPA repository for data.
     */
    private final
    DataRepository dataRepository;

    /**
     * The service responsible for operations on users.
     */
    private final
    UserService userService;

    /**
     * The service reponsible for operations on devices.
     */
    private final
    DeviceService deviceService;

    private final
    UploadHistoryService uploadHistoryService;

    @Autowired
    public RESTController(DataRepository dataRepository, DeviceService deviceService, RESTService restService, UserManagementImpl userService, UploadHistoryService uploadHistoryService) {
        this.uploadHistoryService = uploadHistoryService;
        this.dataRepository = dataRepository;
        this.deviceService = deviceService;
        this.restService = restService;
        this.userService = userService;
    }

    /**
     * The API endpoint for obtaining simple information about a device.
     * <p>
     * URL: /device/{deviceID}/info.json
     * Secured: No
     * Method: POST
     * <p>
     * Sample JSON Return
     * {
     * "id": "64657600-0000-0000-0000-000000000000",
     * "userId": 3,
     * "deviceName": "Development",
     * "deviceLatitude": 0,
     * "deviceLongitude": 0
     * }
     *
     * @param deviceKey The device id associated with the device to be returned.
     * @return JSON formatted device data.
     */
    @RequestMapping(value = "/device/{deviceID}/info.json", method = RequestMethod.POST)
    public
    @ResponseBody
    Device info(@PathVariable("deviceID") String deviceKey) {
        return deviceService.findByKey(deviceKey);
    }

    /**
     * The API endpoint for obtaining an array of upload history for a device.
     * <p>
     * URL: /device/{deviceID}/history.json
     * Secured: No
     * Method: POST
     * <p>
     * <p>
     * Sample JSON Return
     * [
     * {
     * "id": "cd73a33e-6b5a-4e9e-998b-4ab0fb01e41e",
     * "dateTime": null,
     * "duration": 7674,
     * "description": "Data was uploaded successfully"
     * }
     * ]
     *
     * @param deviceID The device id associated with the device to obtain upload history for.
     * @return JSON formatted upload history data.
     */
    @RequestMapping(value = "/device/{deviceID}/history.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<UploadHistory> getHistory(@PathVariable(value = "deviceID") UUID deviceID) {
        Set<UploadHistory> uploadHistories = deviceService.findByKey(deviceID.toString()).getUploadHistories();
        ArrayList<UploadHistory> sortedHistory;
        Collections.sort((sortedHistory = new ArrayList<>(uploadHistories)), (o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        return sortedHistory;
    }

    @RequestMapping(value = "/history/{historyID}/viewed", method = RequestMethod.POST)
    public
    @ResponseBody
    UploadHistory setHistoryViewed(@PathVariable(name = "historyID") UUID historyID) {
        return uploadHistoryService.setViewed(historyID);
    }

    @RequestMapping(value = "/dashboard/data.json", method = RequestMethod.POST)
    public
    @ResponseBody
    InfoCardSerializer populateInfocard()
    {
        return restService.populateInfocards();
    }


    @RequestMapping(value = "/dashboard/devices.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Device> populateDevices()
    {
        return restService.populateDevices();
    }
}
