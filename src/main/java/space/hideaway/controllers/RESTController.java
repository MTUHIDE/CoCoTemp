package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Device;
import space.hideaway.model.UploadHistory;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.services.DeviceService;
import space.hideaway.services.RESTService;
import space.hideaway.util.HistoryUnit;

import java.util.List;


@RestController
public class RESTController
{

    private final
    RESTService restService;

    private final
    DeviceService deviceService;

    @Autowired
    public RESTController(
            DeviceService deviceService,
            RESTService restService)
    {
        this.deviceService = deviceService;
        this.restService = restService;
    }


    /**
     * Obtain information for a specific device.
     * Authenticated: No
     * Method: POST
     * <p>
     * Sample URL: /device/755e67b5-bda1-4ebf-8db4-e59732fb4e1c/info.json
     * <p>
     * Sample Response
     * {
     * "id": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
     * "deviceName": "Piper's Station",
     * "deviceLatitude": 47.116876,
     * "deviceLongitude": -88.543496,
     * "deviceDescription": "My dorm."
     * }
     *
     * @param deviceKey The unique id of the device.
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/device/{deviceID}/info.json", method = RequestMethod.POST)
    public
    @ResponseBody
    Device info(@PathVariable("deviceID") String deviceKey)
    {
        return deviceService.findByKey(deviceKey);
    }

    /**
     * Obtain information for a specific device.
     * Authenticated: No
     * Method: POST
     * <p>
     * Sample URL: /device/755e67b5-bda1-4ebf-8db4-e59732fb4e1c/info.json
     * <p>
     * Sample Response
     * {
     * "deviceCount": 2,
     * "recordCount": 4000,
     * "uploadCount": 4
     * }
     *
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/dashboard/data.json", method = RequestMethod.POST)
    public
    @ResponseBody
    InfoCardSerializer populateInfocard()
    {
        return restService.populateInfocards();
    }

    /**
     * Obtain a list of devices for the currently logged in user.
     * Authenticated: Yes
     * Method: POST
     * <p>
     * Sample URL: /dashboard/devices.json
     * <p>
     * Sample Response
     * [
     * {
     * "id": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
     * "deviceName": "Piper's Station",
     * "deviceLatitude": 47.116876,
     * "deviceLongitude": -88.543496,
     * "deviceDescription": "My dorm."
     * },
     * {
     * "id": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
     * "deviceName": "Wadsworth Station",
     * "deviceLatitude": 47.1167836,
     * "deviceLongitude": -88.5439034,
     * "deviceDescription": "Located outside my window."
     * }
     * ]
     *
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/dashboard/devices.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Device> populateDevices()
    {
        return restService.populateDevices();
    }

    /**
     * Obtain a list of upload history records for the currently logged in user.
     * Authenticated: Yes
     * Method: POST
     * <p>
     * Sample URL: /history.json
     * <p>
     * Sample Response
     * [
     * {
     * "dateTime": 1487497031000,
     * "userID": 1,
     * "id": "4f75f024-7de1-411a-8b1a-d74e2cd8edcb",
     * "deviceID": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
     * "viewed": false,
     * "error": false,
     * "duration": 5546,
     * "description": "Test upload.",
     * "records": 1000
     * },
     * {
     * "dateTime": 1487497071000,
     * "userID": 1,
     * "id": "271a5e98-cfd7-4045-8dc9-0ced586731d4",
     * "deviceID": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
     * "viewed": false,
     * "error": false,
     * "duration": 5686,
     * "description": "Test upload.",
     * "records": 1000
     * },
     * {
     * "dateTime": 1487498249000,
     * "userID": 1,
     * "id": "99359bd8-e799-4dbe-affa-2d9b99151439",
     * "deviceID": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
     * "viewed": false,
     * "error": false,
     * "duration": 6306,
     * "description": "Test upload.",
     * "records": 1000
     * },
     * {
     * "dateTime": 1487498379000,
     * "userID": 1,
     * "id": "fe73a3e5-d4db-4d95-a096-e1fb8adf72bf",
     * "deviceID": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
     * "viewed": false,
     * "error": false,
     * "duration": 5790,
     * "description": "i",
     * "records": 1000
     * }
     * ]
     *
     * @param range The time range of history.
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/history.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<UploadHistory> getUploadHistory(@RequestParam(name = "_range", required = false) String range)
    {
        if (range == null) {return restService.getUploadHistory(HistoryUnit.LAST_30);}
        if (range.equals(HistoryUnit.WEEK.name())) return restService.getUploadHistory(HistoryUnit.WEEK);
        if (range.equals(HistoryUnit.LAST_30.name())) return restService.getUploadHistory(HistoryUnit.LAST_30);
        if (range.equals(HistoryUnit.YEAR.name())) return restService.getUploadHistory(HistoryUnit.YEAR);
        return restService.getUploadHistory(HistoryUnit.LAST_30);
    }
}
