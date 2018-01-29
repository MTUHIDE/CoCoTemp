package space.hideaway.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;
import space.hideaway.services.site.SiteService;
import space.hideaway.services.site.SiteStatisticsService;
import space.hideaway.services.user.UserService;
import space.hideaway.util.FormatUtils;

import java.util.UUID;

/**
 * Edited by Justin Havely
 * 8/18/17
 *
 * Serves the site page to the user.
 */
@Controller
public class SiteController
{

    private final SiteService siteService;
    private final SiteStatisticsService siteStatisticsService;
    private final UserService userService;

    @Autowired
    public SiteController(
            SiteService siteService,
            SiteStatisticsService siteStatisticsService,
            UserService userService)
    {
        this.siteService = siteService;
        this.siteStatisticsService = siteStatisticsService;
        this.userService = userService;
    }

    /**
     * The endpoint for site pages.
     *
     * URL: /site/{siteID}
     * Secured: No
     * Method: GET
     *
     * @param model The model maintained by Spring.
     * @param siteID The ID of the associated site to be rendered.
     * @return The name of the site template.
     */
    @RequestMapping(value = "/site/{siteID}")
    public String showSite(
            Model model,
            @PathVariable(value = "siteID") UUID siteID)
    {

        Site site = siteService.findByKey(siteID.toString());
        model.addAttribute("site", site);
        model.addAttribute("siteID", site.getId());
        model.addAttribute("user", site.getUser());

        // Values for the site page title card.
        SiteStatistics siteStatistics = siteStatisticsService.getMostRecent(site);
        model.addAttribute("max", FormatUtils.doubleToVisualString(siteStatistics.getAllMax()));
        model.addAttribute("min", FormatUtils.doubleToVisualString(siteStatistics.getAllMin()));
        model.addAttribute("avg", FormatUtils.doubleToVisualString(siteStatistics.getAllAvg()));
        model.addAttribute("deviation", FormatUtils.doubleToVisualString(siteStatistics.getAllDeviation()));

        return "station";
    }

}
