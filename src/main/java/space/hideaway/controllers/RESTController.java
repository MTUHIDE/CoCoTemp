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
import java.util.Set;
import java.util.UUID;


@RestController
public class RESTController {


    @Autowired
    RESTService restService;

    @Autowired
    DataRepository dataRepository;

    @Autowired
    UserManagementImpl userManagementImpl;

    @Autowired
    DashboardServiceImplementation dashboardServiceImplementation;

    @Autowired
    UploadHistoryService uploadHistoryService;

    @Autowired
    DeviceService deviceService;


    @RequestMapping(value = "/devicePoints.json", method = RequestMethod.POST)
    public
    @ResponseBody
    String geoJSON() {
        return restService.getGeoJsonForLastRecordedTemperature();
    }

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/{deviceID}", method = RequestMethod.GET)
    public DataTablesOutput<Data> getData(@PathVariable(name = "deviceID") UUID deviceID, @Valid DataTablesInput dataTablesInput) {
        return dataRepository.findAll(dataTablesInput, null, (root, query, cb) -> cb.equal(root.get("deviceID"), deviceID));
    }

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public DataTablesOutput<Data> getData(@Valid DataTablesInput dataTablesInput) {
        return dataRepository.findAll(dataTablesInput, null, (root, query, cb) -> cb.equal(root.get("userID"), userManagementImpl.getCurrentLoggedInUser().getId()));
    }

    @RequestMapping(value = "/device/{deviceID}/info.json", method = RequestMethod.POST)
    public
    @ResponseBody
    Device statistics(@PathVariable("deviceID") String deviceKey) {
        return deviceService.findByKey(deviceKey);
    }

    @RequestMapping(value = "/device/{deviceID}/history.json", method = RequestMethod.POST)
    public
    @ResponseBody
    Set<UploadHistory> getHistory(@PathVariable(value = "deviceID") UUID deviceID) {
        System.out.println("Running...");
        Device device = deviceService.findByKey(deviceID.toString());
        return device.getUploadHistories();
    }

}
