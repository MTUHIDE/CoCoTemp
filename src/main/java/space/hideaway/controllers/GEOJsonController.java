package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.services.GEOJsonService;


/**
 * HIDE CoCoTemp 2016
 * The controller for the device points API.
 *
 * @author Piper Dougherty
 */
@Controller
public class GEOJsonController {

    /**
     * The service reposnible for creating various GEOJson structures for use by leaflet.
     */
    @Autowired
    GEOJsonService geoJsonService;

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
        return geoJsonService.getGeoJsonForLastRecordedTemperature();
    }

}
