package space.hideaway.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.services.UploadService;
import space.hideaway.services.UserService;

import java.util.Set;
import java.util.UUID;


@Controller
public class UploadController {


    private final UploadService uploadService;
    private final UserService userService;

    Logger logger = Logger.getLogger(getClass());


    @Autowired
    public UploadController(UploadService uploadService, UserService userService)
    {
        this.uploadService = uploadService;
        this.userService = userService;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String showUploadForm(Model model)
    {
        User currentLoggedInUser = userService.getCurrentLoggedInUser();
        Set<Device> deviceSet = currentLoggedInUser.getDeviceSet();
        model.addAttribute("devices", deviceSet);
        return "upload";
    }


    /**
     * The API endpoint for loading a CSV file into the database.
     * <p>
     * URL: /upload/{deviceID}
     * Secured: Yes
     * Method: POST
     * <p>
     * TODO: Sample JSON response.
     *
     * @param deviceID The ID of the device the uploaded data is associated with.
     * @param file     The file uploaded by the user.
     * @return JSON response indicating the status of the upload.
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFile(
            @RequestParam(value = "deviceID") UUID deviceID,
            @RequestParam(value = "csvData") MultipartFile file,
            @RequestParam(value = "description") String description
    ) {
        logger.info("Upload request incoming, parse file starting.");
        return uploadService.setMultipartFile(file).parseFile(deviceID.toString(), description);
    }

}
