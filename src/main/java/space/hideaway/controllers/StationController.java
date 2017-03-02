package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.model.Device;
import space.hideaway.model.StationStatistics;
import space.hideaway.services.DeviceService;
import space.hideaway.services.StationStatisticsService;
import space.hideaway.util.FormatUtils;

import java.util.UUID;

@Controller
public class StationController
{

    private final DeviceService deviceService;
    private final StationStatisticsService stationStatisticsService;

    @Autowired
    public StationController(DeviceService deviceService, StationStatisticsService stationStatisticsService)
    {
        this.deviceService = deviceService;
        this.stationStatisticsService = stationStatisticsService;
    }

    /**
     * The endpoint for station pages.
     * <p>
     * URL: /device/{deviceID}
     * Secured: No
     * Method: GET
     *
     * @param model    The model maintained by Spring.
     * @param deviceID The ID of the associated device to be rendered.
     * @return The name of the station view template.
     */
    @RequestMapping(value = "/device/{deviceID}")
    public String showDevice(Model model, @PathVariable(value = "deviceID") UUID deviceID)
    {
        Device device = deviceService.findByKey(deviceID.toString());
        model.addAttribute("device", device);
        model.addAttribute("deviceID", device.getId());
        model.addAttribute("user", device.getUser());

        StationStatistics deviceStatistics = stationStatisticsService.getMostRecent(device);
        model.addAttribute("max", FormatUtils.doubleToVisualString(deviceStatistics.getAllMax()));
        model.addAttribute("min", FormatUtils.doubleToVisualString(deviceStatistics.getAllMin()));
        model.addAttribute("avg", FormatUtils.doubleToVisualString(deviceStatistics.getAllAvg()));
        model.addAttribute("deviation", FormatUtils.doubleToVisualString(deviceStatistics.getAllDeviation()));

        return "station";
    }


}
