package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.model.upload.UploadHistory;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.services.RESTService;
import space.hideaway.services.site.SiteMetadataService;
import space.hideaway.services.site.SiteService;
import space.hideaway.util.HistoryUnit;
import space.hideaway.util.SortingUtils;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Edited by Justin Havely
 * 8/18/17
 *
 * Serves the REST API requests.
 */
@RestController
public class RESTController
{

    private final RESTService restService;
    private final SiteService siteService;
    private final SiteMetadataService siteMetadataService;

    @Autowired
    public RESTController(
            SiteService siteService,
            RESTService restService,
            SiteMetadataService siteMetadataService
            )
    {
        this.siteService = siteService;
        this.restService = restService;
        this.siteMetadataService = siteMetadataService;
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
     * "siteDescription": "My dorm.",
     * "deviceSet": [
     *    {
     *       "id": "b5af1466-6f81-4d75-bd28-2d913e8b6627",
     *       "manufacture_num": "1234231512",
     *       "type": "iButton",
     *       "siteID": "c9e3ba13-c0b1-46ac-b96a-432ef5a9425c"
     *    }
     * ]
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
     * Obtain site, upload, and record count for the currently logged in user.
     * Authenticated: YES
     * Method: POST
     * <p>
     * Sample URL: /dashboard/data.json
     * <p>
     * Sample Response
     * {
     * "siteCount": 2,
     * "recordCount": 4000,
     * "uploadCount": 4,
     * "deviceCount": 1
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
     * "siteDescription": "My dorm.",
     * "deviceSet": []
     * },
     * {
     * "id": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
     * "siteName": "Wadsworth Station",
     * "siteLatitude": 47.1167836,
     * "siteLongitude": -88.5439034,
     * "siteDescription": "Located outside my window.",
     * "deviceSet": [
     * {
     *      "id": "b5af1466-6f81-4d75-bd28-2d913e8b6627",
     *      "manufacture_num": "1234231512",
     *      "type": "iButton",
     *      "siteID": "c9e3ba13-c0b1-46ac-b96a-432ef5a9425c"
     * }
     * ]
     * }
     * ]
     *
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/dashboard/sites.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Site> populateSites()
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
    public
    @ResponseBody
    List<Device> populateDevices(){
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
     * "deviceID": 148749349000,
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
     * "deviceID": null,
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
     * "deviceID": null,
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
     * "deviceID": 1487498249000,
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

    /**
     * Obtain a list temperature records for a site.
     * Authenticated: NO
     * Method: GET
     * <p>
     * Sample URL: /site/c9e3ba13-c0b1-46ac-b96a-432ef5a9425c/temperature.json
     * <p>
     * Sample Response
     *    {
     * "id": "1c99e0e5-72d0-42fd-a2ca-8d95e9ccc14f",
     * "userID": 1,
     * "siteID": "c9e3ba13-c0b1-46ac-b96a-432ef5a9425c",
     * "dateTime": 1502682481000,
     * "temperature": 13,
     * "deviceID": null
     * },
     * {
     * "id": "72a01902-3397-4a7c-8b7a-1201b94ac6be",
     * "userID": 1,
     * "siteID": "c9e3ba13-c0b1-46ac-b96a-432ef5a9425c",
     * "dateTime": 1502678881000,
     * "temperature": 14,
     * "deviceID": 432ef5a9425c
     * },
     * {
     * "id": "fd36bbdf-e0a7-459a-8755-2d4102c30ab3",
     * "userID": 1,
     * "siteID": "c9e3ba13-c0b1-46ac-b96a-432ef5a9425c",
     * "dateTime": 1502675281000,
     * "temperature": 12,
     * "deviceID": 432ef5a9425c
     * },
     * {
     * "id": "0a38ec66-a164-4221-9193-2c2fa7143a9c",
     * "userID": 1,
     * "siteID": "c9e3ba13-c0b1-46ac-b96a-432ef5a9425c",
     * "dateTime": 1502671681000,
     * "temperature": 30,
     * "deviceID": 432ef5a9425c
     * }
     * ]
     *
     * @param siteID The unique id of the site.
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/site/{siteID}/temperature.json", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Data> getTemperaturePoints(@PathVariable("siteID") String siteID)
    {
        return SortingUtils.sortMostRecentFirst(siteService.findByKey(siteID).getDataSet());
    }

    /**
     * Obtain a list of all register sites.
     * Authenticated: NO
     * Method: POST
     * <p>
     * Sample URL: /sites.json
     * <p>
     * Sample Response
     * [
     * {
     * "id": "755e67b5-bda1-4ebf-8db4-e59732fb4e1c",
     * "siteName": "Piper's Station",
     * "siteLatitude": 47.116876,
     * "siteLongitude": -88.543496,
     * "siteDescription": "My dorm.",
     * "deviceSet": []
     * },
     * {
     * "id": "a2f4b833-74de-492c-9348-85e5ab2e2a97",
     * "siteName": "Wadsworth Station",
     * "siteLatitude": 47.1167836,
     * "siteLongitude": -88.5439034,
     * "siteDescription": "Located outside my window.",
     * "deviceSet": [
     * {
     *      "id": "b5af1466-6f81-4d75-bd28-2d913e8b6627",
     *      "manufacture_num": "1234231512",
     *      "type": "iButton",
     *      "siteID": "c9e3ba13-c0b1-46ac-b96a-432ef5a9425c"
     * }
     * ]
     * }
     * ]
     *
     * @return @see the sample response above.
     */
    @RequestMapping(value = "/sites.json", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Site> getSitePoints()
    {
        return siteService.getAllSites();
    }


    @RequestMapping(value = "/search.json", params = {"type=site"})
    @Transactional
    public List<Site> renderSearchWithKeywordAndSpatial(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "range", required = false) String range) {
        return restService.populateSitesByQuery(query, location, range);
    }

    @RequestMapping(value = "/sitemetadata.json", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SiteMetadata> getSiteMetadataPoints()
    {
        List<SiteMetadata> t = siteMetadataService.getAllSiteMetadata();
        return t;
    }


}
