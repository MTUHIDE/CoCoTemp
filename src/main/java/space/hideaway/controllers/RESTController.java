package space.hideaway.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Data;
import space.hideaway.repositories.DataRepository;
import space.hideaway.services.RESTService;
import space.hideaway.services.UserServiceImplementation;

import javax.validation.Valid;
import java.util.UUID;


/**
 * HIDE CoCoTemp 2016
 * The controller for the device points API.
 *
 * @author Piper Dougherty
 */
@RestController
public class RESTController {

    /**
     * The service reposnible for creating various GEOJson structures for use by leaflet.
     */
    @Autowired
    RESTService restService;

    @Autowired
    DataRepository dataRepository;

    @Autowired
    UserServiceImplementation userServiceImplementation;

    /**
     * Obtain a GEOJson formatted JSON structure with a single point for each device, so long
     * as the points were recorded the same day. Used on the home page to give a brief overview
     * as to the current temperature spread across the map. Data points are only shown on the map
     * for that day, to prevent a ridiculous amount of points being placed.
     *
     * @return A GEOJson structure of "features."
     */
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
        return dataRepository.findAll(dataTablesInput, null, (root, query, cb) -> cb.equal(root.get("userID"), userServiceImplementation.getCurrentLoggedInUser().getId()));
    }

}
