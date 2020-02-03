package space.hideaway.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.model.User;
import space.hideaway.services.site.SiteStatisticsService;
import space.hideaway.services.user.UserService;


@Controller
public class NOAASiteController {

    private final SiteStatisticsService siteStatisticsService;
    private final UserService userService;

    @Autowired
    private Environment env;

    @Autowired
    public NOAASiteController(
            SiteStatisticsService siteStatisticsService,
            UserService userService)
    {
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
    @RequestMapping(value = "/NOAASite/{siteID}")
    public String showSite(
            Model model,
            @PathVariable(value = "siteID") String siteID) throws Exception {

        User user = userService.getCurrentLoggedInUser();
        char tempStandard = 'F';
        model.addAttribute("id", siteID);

        if(user!=null)
        {
            tempStandard=user.getTempStandard();
        }
            model.addAttribute("tempstandard",tempStandard);
            model.addAttribute("NOAAToken",env.getProperty("spring.NOAA.token"));

        return "NOAAStation";
    }

}
