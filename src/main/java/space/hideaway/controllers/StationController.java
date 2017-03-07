package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.model.Site;
import space.hideaway.model.SiteStatistics;
import space.hideaway.services.SiteService;
import space.hideaway.services.StationStatisticsService;
import space.hideaway.util.FormatUtils;

import java.util.UUID;

@Controller
public class StationController
{

    private final SiteService siteService;
    private final StationStatisticsService stationStatisticsService;

    @Autowired
    public StationController(SiteService siteService, StationStatisticsService stationStatisticsService)
    {
        this.siteService = siteService;
        this.stationStatisticsService = stationStatisticsService;
    }

    /**
     * The endpoint for station pages.
     * <p>
     * URL: /site/{siteID}
     * Secured: No
     * Method: GET
     *
     * @param model    The model maintained by Spring.
     * @param siteID The ID of the associated site to be rendered.
     * @return The name of the station view template.
     */
    @RequestMapping(value = "/site/{siteID}")
    public String showSite(Model model, @PathVariable(value = "siteID") UUID siteID)
    {
        Site site = siteService.findByKey(siteID.toString());
        model.addAttribute("site", site);
        model.addAttribute("siteID", site.getId());
        model.addAttribute("user", site.getUser());

        SiteStatistics siteStatistics = stationStatisticsService.getMostRecent(site);
        model.addAttribute("max", FormatUtils.doubleToVisualString(siteStatistics.getAllMax()));
        model.addAttribute("min", FormatUtils.doubleToVisualString(siteStatistics.getAllMin()));
        model.addAttribute("avg", FormatUtils.doubleToVisualString(siteStatistics.getAllAvg()));
        model.addAttribute("deviation", FormatUtils.doubleToVisualString(siteStatistics.getAllDeviation()));

        return "station";
    }


}
