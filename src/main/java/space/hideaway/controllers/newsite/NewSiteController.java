package space.hideaway.controllers.newsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import space.hideaway.model.globe.Globe;
import space.hideaway.model.Site;
import space.hideaway.repositories.GlobeRepository;
import space.hideaway.services.SiteService;
import space.hideaway.validation.NewSiteValidator;

import java.util.List;

/**
 * The controller for the multi-page new site registration. Contains a session attribute "site"
 * to persist a template site across the multiple pages of the new site registration route.
 */
@Controller
@RequestMapping("/settings/new")
@SessionAttributes("site")
public class NewSiteController
{

    /**
     * The validator for a site.
     */
    private final
    NewSiteValidator newSiteValidator;

    private final GlobeRepository globeRepository;

    /**
     * The service responsible for CRUD operations on sites.
     */
    private final
    SiteService siteService;

    @Autowired
    public NewSiteController(
            NewSiteValidator newSiteValidator,
            SiteService siteService,
            GlobeRepository globeRepository)
    {
        this.globeRepository = globeRepository;
        this.newSiteValidator = newSiteValidator;
        this.siteService = siteService;
    }

    /**
     * Load the initial page of the new site registration route. Injects a blank
     * site into the model for the user to manipulate over the next few pages.
     * The session begins with this page.
     *
     * @param modelMap The model maintained by Spring for the new site route.
     * @return The path to the new-site-form template.
     */
    @RequestMapping
    public String initialPage(final ModelMap modelMap)
    {
        //Add a blank site to the session's model.
        modelMap.addAttribute("site", new Site());
        modelMap.addAttribute("globe", new Globe());
        return "new-site/new-site-form";
    }

    /**
     * The second page of the new site registration route.
     *
     * @param site          The site that exists in the model with fields populated by what the user
     *                      entered into the form on the initial page.
     * @param bindingResult The error module for this page, allows bindings between the validation module
     *                      and the actual page.
     * @return The path to the site-questionnaire template, or the path to the new-site-form
     * if validation has failed on fields from the first page.
     */
    @RequestMapping(params = "_questions", method = RequestMethod.POST)
    public String questionPage(
            final @ModelAttribute("site") Site site,
            final BindingResult bindingResult
    )
    {
        newSiteValidator.validate(site, bindingResult);
        if (bindingResult.hasErrors())
        {
            return "new-site/new-site-form";
        }

        return "new-site/site-questionnaire";
    }

    @RequestMapping(params = "_globe", method = RequestMethod.POST)
    public String globePage(
            final @ModelAttribute("site") Site site,
            final BindingResult bindingResult,
            final @ModelAttribute("globe") Globe globe
    )
    {
        //Perform validation on the fields, and redirect to the previous page if errors are present.
        newSiteValidator.validateFinal(site, bindingResult);
        if (bindingResult.hasErrors())
        {
            return "new-site/site-questionnaire";
        }

        return "new-site/globe-questionnaire";
    }


    /**
     * The final page of the new site registration route. Validation is performed on fields from the previous page
     * and the site is persisted if no issues are present.
     *
     * @param site          The site that exists in the model with fields populated by what the user
     *                      entered into the form on the second page.
     * @param sessionStatus The session module for the new site registration route.
     * @return A redirect command to the dashboard, or the path to the site-questionnaire if validation
     * has failed on fields from the previous page.
     */
    @RequestMapping(params = "_finish", method = RequestMethod.POST)
    public String createSite(@ModelAttribute("site") Site site, SessionStatus sessionStatus) {

        //Persist the site.
        siteService.save(site);
        //Set the session complete, as the site has been safely persisted.
        sessionStatus.setComplete();

        //Redirect to the dashboard.
        return "redirect:/dashboard";
    }

    @RequestMapping(params = "_finish_globe", method = RequestMethod.POST)
    public String createGlobeSite(@ModelAttribute("site") Site site, SessionStatus sessionStatus,
                                  @RequestParam("answers") String[] answer) {

        //Persist the site.
        siteService.save(site);

        //I'm sure is a better way of doing this
        for (byte i = 0; i < answer.length; i++) {
            Globe globe = new Globe();
            globe.setSite(site);
            globe.setSiteID(site.getId());
            globe.setAnswer(answer[i]);
            globe.setQuestion_number((byte)(i + 1));
            globeRepository.save(globe);
        }

        //Set the session complete, as the site has been safely persisted.
        sessionStatus.setComplete();

        //Redirect to the dashboard.
        return "redirect:/dashboard";
    }
}
