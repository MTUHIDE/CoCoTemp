package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.services.UploadService;

/**
 * HIDE CoCoTemp 2016
 * Class responsible for routing file uploads.
 *
 * @author Piper Dougherty
 */
@Controller
public class UploadController {

    /**
     * The service responsible for parsing files and inserting them into the database.
     */
    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }


    /**
     * API for inserting a file into the database. Returns the status in
     * JSON format.
     * <p>
     * Example JSON structure for a successful upload.
     * {
     * status: "uploaded"
     * }
     * <p>
     * Example JSON structure for an unsuccessful upload.
     * {
     * status: "failed"
     * }
     *
     * @param deviceKey The unique ID of the device.
     * @param file      The file to be inserted.
     * @return JSON representing the status of the upload.
     */
    @RequestMapping(value = "/upload/{deviceKey}", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFile(
            @PathVariable(value = "deviceKey") String deviceKey,
            @RequestParam(value = "file") MultipartFile file
    ) {
        return uploadService.setMultipartFile(file).parseFile(deviceKey);
    }

}
