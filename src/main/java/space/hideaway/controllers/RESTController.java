package space.hideaway.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import space.hideaway.model.Data;
import space.hideaway.repositories.DataRepository;
import space.hideaway.services.RESTService;

import javax.validation.Valid;


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
    @RequestMapping(value = "/dataPoints.json", method = RequestMethod.GET)
    public DataTablesOutput<Data> getData(@Valid DataTablesInput dataTablesInput) {
        return dataRepository.findAll(dataTablesInput);
    }

}
