package space.hideaway.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.repositories.site.SiteMetadataRepository;
import space.hideaway.repositories.site.SiteRepository;
import space.hideaway.services.site.SiteMetadataService;
import space.hideaway.services.site.SiteService;
import space.hideaway.validation.SiteValidator;

import java.util.UUID;
/*
    This controller is for allowing the user to redo the Globe survey for a given site
 */
@Controller
@RequestMapping("/edit_globe")
public class EditGlobeController {

    private final SiteValidator siteValidator;
    private final SiteService siteService;
    private final SiteMetadataService siteMetadataService;
    private final SiteRepository siteRepository;
    private final SiteMetadataRepository siteMetadataRepository;

    @Autowired
    public EditGlobeController(
            SiteValidator siteValidator,
            SiteService siteService,
            SiteMetadataService siteMetadataService,
            SiteRepository siteRepository,
            SiteMetadataRepository siteMetadataRepository
            )
    {
        this.siteValidator = siteValidator;
        this.siteService = siteService;
        this.siteMetadataService = siteMetadataService;
        this.siteMetadataRepository = siteMetadataRepository;
        this.siteRepository = siteRepository;

    }

    private SiteMetadata currentMetadata;
    @RequestMapping
    public String start(final ModelMap modelMap, @RequestParam(value = "siteID") String id){

        currentMetadata = siteMetadataRepository.findBySiteID(UUID.fromString(id));
        return "edit_globe_metadata";
    }

    @RequestMapping(params = "_update", method = RequestMethod.POST)
    public String update(
            @ModelAttribute("site") Site site,
            @ModelAttribute("metadata") SiteMetadata metadata
    ){
        currentMetadata.setEnvironment(metadata.getEnvironment());
        currentMetadata.setPurpose(metadata.getPurpose());
        currentMetadata.setHeightAboveGround(metadata.getHeightAboveGround());
        currentMetadata.setHeightAboveFloor(metadata.getHeightAboveFloor());
        currentMetadata.setEnclosurePercentage(metadata.getEnclosurePercentage());
        currentMetadata.setNearestAirflowObstacle(metadata.getNearestAirflowObstacle());
        currentMetadata.setNearestObstacleDegrees(metadata.getNearestObstacleDegrees());
        currentMetadata.setObstacleType(metadata.getObstacleType());
        currentMetadata.setAreaAroundSensor(metadata.getAreaAroundSensor());
        currentMetadata.setRiparianArea(metadata.isRiparianArea());
        currentMetadata.setMaxNightTime(metadata.getMaxNightTime());
        currentMetadata.setMinNightTime(metadata.getMinNightTime());
        currentMetadata.setCanopyType(metadata.getCanopyType());
        currentMetadata.setSkyViewFactor(metadata.getSkyViewFactor());
        currentMetadata.setSlope(metadata.getSlope());
        currentMetadata.setSlopeDirection(metadata.getSlopeDirection());
        currentMetadata.setNearestWater(metadata.getNearestWater());
        currentMetadata.setWaterDistance(metadata.getWaterDistance());
        currentMetadata.setWaterDirection(metadata.getWaterDirection());

        siteMetadataService.save(currentMetadata);
        return "redirect:/settings";
    }

    @RequestMapping(params = "_complete", method = RequestMethod.POST)
    public String returnToSettings(){
        return "redirect:/settings";
    }

}
