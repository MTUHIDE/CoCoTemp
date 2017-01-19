package space.hideaway.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.UploadHistory;
import space.hideaway.repositories.DataRepository;
import space.hideaway.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;


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
     * The API endpoint for getting GEOJson data for the average recorded temperature for each device
     * on the current day.
     * <p>
     * URL: /devicePoints.json
     * Secured: No
     * Method: POST
     *
     * @return A GeoJSON object.
     */
    @RequestMapping(value = "/devicePoints.json", method = RequestMethod.POST)
    public
    @ResponseBody
    String geoJSON() {
        return restService.getGeoJsonForLastRecordedTemperature();
    }

    /**
     * DataTables API for getting all data points for a specific device.
     * <p>
     * URL: /data/{deviceID}
     * Secured: No
     * Method: GET
     *
     * @param deviceID        The id of the device to obtain the data for.
     * @param dataTablesInput Parameters generated by the datatables plugin, contains
     *                        information about pagination, search, etc...
     * @return JSON data formatted for use by the datatables plugin.
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/{deviceID}", method = RequestMethod.GET)
    public DataTablesOutput<Data> getData(@PathVariable(name = "deviceID") UUID deviceID, @Valid DataTablesInput dataTablesInput) {
        return dataRepository.findAll(dataTablesInput, null, (root, query, cb) -> cb.equal(root.get("deviceID"), deviceID));
    }

    /**
     * Datatables API for getting all data points for a specific user.
     * <p>
     * URL: /data
     * Secured: Yes
     * Method: GET
     *
     * @param dataTablesInput Parameters generated by the datatables plugin, contains
     *                        information about pagination, search, etc...
     * @return JSON data formatted for use by the datatables plugin.
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public DataTablesOutput<Data> getData(@Valid DataTablesInput dataTablesInput) {
        return dataRepository.findAll(dataTablesInput, null, (root, query, cb) -> cb.equal(root.get("userID"), userService.getCurrentLoggedInUser().getId()));
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

}