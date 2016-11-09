package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.services.GEOJsonService;

/**
 * Created by dough on 11/9/2016.
 */
@Controller
public class GEOJsonController {

    @Autowired
    GEOJsonService geoJsonService;

    @RequestMapping(value = "/devicePoints.json", method = RequestMethod.POST)
    public
    @ResponseBody
    String geoJSON() {
        return geoJsonService.getGeoJsonForLastRecordedTemperature();
    }

}
