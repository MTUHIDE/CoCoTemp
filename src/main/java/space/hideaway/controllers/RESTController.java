package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.Site;
import space.hideaway.model.UploadHistory;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.services.RESTService;
import space.hideaway.services.SiteService;
import space.hideaway.util.HistoryUnit;
import space.hideaway.util.SortingUtils;

import java.util.List;


@RestController
public class RESTController
{

    private final
    RESTService restService;

    private final
    SiteService siteService;

    @Autowired
    public RESTController(
            SiteService siteService,
            RESTService restService)
    {
        this.siteService = siteService;
        this.restService = restService;
    }


    /**
     * Obtain information for a specific site.
     * Authenticated: No
     * Method: POST
     * <p>
     * Sample URL: /site/755e67b5-bda1-4ebf-8db4-e59732fb4e1c/info.json
     * <p>
     * Sample Response
     * {
     * "id": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
     * "siteName": "Piper's Station",
     * "siteLatitude": 47.116876,
     * "siteLongitude": -88.543496,
     * "siteDescription": "My dorm."
     * }
     *
     * @param siteKey The unique id of the site.
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/site/{siteID}/info.json", method = RequestMethod.POST)
    public
    @ResponseBody
    Site info(@PathVariable("siteID") String siteKey)
    {
        return siteService.findByKey(siteKey);
    }

    /**
     * Obtain information for a specific site.
     * Authenticated: No
     * Method: POST
     * <p>
     * Sample URL: /site/755e67b5-bda1-4ebf-8db4-e59732fb4e1c/info.json
     * <p>
     * Sample Response
     * {
     * "siteCount": 2,
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
     * Obtain a list of sites for the currently logged in user.
     * Authenticated: Yes
     * Method: POST
     * <p>
     * Sample URL: /dashboard/sites.json
     * <p>
     * Sample Response
     * [
     * {
     * "id": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
     * "siteName": "Piper's Station",
     * "siteLatitude": 47.116876,
     * "siteLongitude": -88.543496,
     * "siteDescription": "My dorm."
     * },
     * {
     * "id": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
     * "siteName": "Wadsworth Station",
     * "siteLatitude": 47.1167836,
     * "siteLongitude": -88.5439034,
     * "siteDescription": "Located outside my window."
     * }
     * ]
     *
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/dashboard/sites.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Site> populatesites()
    {
        return restService.populateSites();
    }

    /**
     * Obtain a list of devices for the currently logged in user.
     * Authenticated: Yes
     * Method: POST
     * <p>
     * Sample URL: /dashboard/devices.json
     * <p>
     * Sample Response
     *
     * [
     * {
     * "id": "683e3aad-91d7-4446-8fcb-476100b0e295",
     * "manufacture_num": "123548",
     * "type": "iButton",
     * "siteID": "0cb903f0-5877-4dbb-8d13-cd985079210d",
     * }
     * ]
     *
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/dashboard/devices.json", method = RequestMethod.POST)
    public @ResponseBody List<Device> populateDevices(){
        return restService.poplateDevices();
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
     * "siteID": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
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
     * "siteID": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
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
     * "siteID": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
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
     * "siteID": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
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


    @RequestMapping(value = "/site/{siteID}/temperature.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Data> getTemperaturePoints(@PathVariable("siteID") String siteID)
    {
        return SortingUtils.sortMostRecentFirst(siteService.findByKey(siteID).getDataSet());
    }
}
