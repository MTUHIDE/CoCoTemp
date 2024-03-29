package space.hideaway.controllers;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.model.site.Site;
import space.hideaway.model.User;
import space.hideaway.services.upload.UploadService;
import space.hideaway.services.user.UserService;

import java.util.Set;
import java.util.UUID;

/**
 * Created by dough
 * 1/25/2017
 *
 * Edited by Justin Havely
 * 8/18/17
 *
 * Serves the upload page to the user.
 */
@Controller
public class UploadController
{

    private final UploadService uploadService;
    private final UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UploadController(
            UploadService uploadService,
            UserService userService)
    {
        this.uploadService = uploadService;
        this.userService = userService;
    }

    /**
     * The mapping for the upload page.
     *
     * @param model The Spring model for the upload page.
     * @return The template for the upload page.
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String showUploadForm(Model model)
    {
        User currentLoggedInUser = userService.getCurrentLoggedInUser();
        Set<Site> siteSet = currentLoggedInUser.getSiteSet();
        model.addAttribute("sites", siteSet);
        return "upload";
    }

    /**
     * The API endpoint for loading a CSV file into the database.
     * <p>
     * URL: /upload/{siteID}
     * Secured: Yes
     * Method: POST
     * <p>
     * TODO: Sample JSON response.
     *
     * @param siteID The ID of the site the uploaded data is associated with.
     * @param file The file uploaded by the user.
     * @return JSON response indicating the status of the upload.
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    String uploadFile(
            @RequestParam(value = "siteID") UUID siteID,
            @RequestParam(value = "csvData") MultipartFile file,
            @RequestParam(value = "description") String description)
    {
        logger.info("Upload request incoming, parse file starting.");
        uploadService.setMultipartFile(file).parseFile(siteID.toString(), description);
        return "redirect:/dashboard";
    }

}
