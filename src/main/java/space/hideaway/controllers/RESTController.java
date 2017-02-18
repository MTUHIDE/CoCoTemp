package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Device;
import space.hideaway.model.UploadHistory;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.repositories.DataRepository;
import space.hideaway.services.*;
import space.hideaway.util.HistoryUnit;

import java.util.List;


@RestController
public class RESTController
{


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
    public RESTController(
            DataRepository dataRepository,
            DeviceService deviceService,
            RESTService restService,
            UserManagementImpl userService,
            UploadHistoryService uploadHistoryService)
    {
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
    Device info(@PathVariable("deviceID") String deviceKey)
    {
        return deviceService.findByKey(deviceKey);
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

    @RequestMapping(value = "/history.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<UploadHistory> getUploadHistory(@RequestParam(name = "_range") String range)
    {
        if (range.equals(HistoryUnit.WEEK.name())) return restService.getUploadHistory(HistoryUnit.WEEK);
        if (range.equals(HistoryUnit.MONTH.name())) return restService.getUploadHistory(HistoryUnit.MONTH);
        if (range.equals(HistoryUnit.YEAR.name())) return restService.getUploadHistory(HistoryUnit.YEAR);
        return restService.getUploadHistory(HistoryUnit.MONTH);
    }
}
