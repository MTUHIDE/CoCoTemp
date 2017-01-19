package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.services.UploadService;


@Controller
public class UploadController {


    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
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
    @RequestMapping(value = "/upload/{deviceID}", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFile(
            @PathVariable(value = "deviceID") String deviceID,
            @RequestParam(value = "file") MultipartFile file
    ) {
        return uploadService.setMultipartFile(file).parseFile(deviceID);
    }

}
