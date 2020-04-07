package space.hideaway.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.repositories.site.SiteMetadataRepository;
import space.hideaway.services.site.SiteService;
import space.hideaway.services.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MicroClimateController {
    @Autowired
    private Environment env;

    private final UserService userService;
    private final SiteService siteService;
    private final SiteMetadataRepository siteMetadataRepository;

    @Autowired
    public MicroClimateController(UserService userService,SiteService siteService,SiteMetadataRepository siteMetadataRepository){
        this.userService=userService;
        this.siteMetadataRepository = siteMetadataRepository;
        this.siteService=siteService;
    }

    @RequestMapping(value = "/microclimate")
    public String showMicroclimate(Model model){

        User user = userService.getCurrentLoggedInUser();
        Long userId = (long) 0;
        char tempStandard = 'F';
        if(user!=null) {
            userId = user.getId();
            tempStandard=user.getTempStandard();
        }
        model.addAttribute("tempstandard",tempStandard);
        model.addAttribute("NOAAToken",env.getProperty("spring.NOAA.token"));
        model.addAttribute("userID",userId);
        return "microclimate";
    }

    @RequestMapping(value="/microclimate/recentSites/{userID}/{siteID}/{func}")
    public String updateCoCoSites(@PathVariable("siteID") String siteKey, @PathVariable("userID") String userID,@PathVariable("func") String func)
    {
        long userId = (long) Integer.valueOf(userID);
        int function = Integer.valueOf(func);
        userService.updateCoCoSites(userId,siteKey,function);
        return "microclimate";
    }

    @RequestMapping(value="/microclimate/NOAASites")
    public String updateNOAASites(@RequestParam("siteid") String siteKey, @RequestParam("id") String userID, @RequestParam("func") String func,
                                  @RequestParam("lon") String longitude, @RequestParam("lat") String Latitude, @RequestParam("name") String name)
    {
        long userId = (long) Integer.valueOf(userID);
        double latit = Double.valueOf(Latitude);
        double longit = Double.valueOf(longitude);
        int function = Integer.valueOf(func);
        userService.updateNOAASites(userId,siteKey,function,latit,longit,name);
        return "microclimate";
    }

    @RequestMapping(value = "/microclimate/NOAArecentSites/{userID}")
    public @ResponseBody
    String getRecentNOAASites(@PathVariable("userID") String userID)
    {
        long userId = (long) Integer.valueOf(userID);
        String userString = userService.getNOAASites(userId);
        return userString;
    }

    @RequestMapping(value = "/microclimate/recentSites/{userID}")
    public @ResponseBody
    List<Object> getRecentCoCoSites(@PathVariable("userID") String userID)
    {
        long userId = (long) Integer.valueOf(userID);
        String userString = userService.getCoCoSites(userId);
        if(userString!=null)
        {
            List list = new ArrayList<Object>();

            String [] cocoSites= userString.split(",");
            Site site=  null;
            SiteMetadata siteMetadata =null;
            UUID siteID = null;
            for(int i=0;i<cocoSites.length;i++)
            {
                Object[] pair = new Object[2];
                site = siteService.findByKey(cocoSites[i]);
               siteID = UUID.fromString(cocoSites[i]);
               siteMetadata = siteMetadataRepository.findBySiteID(siteID);
               pair[0] = site;
               pair[1] =siteMetadata;
               list.add(pair);
            }
            return list;

        }
        return null;
    }

}
