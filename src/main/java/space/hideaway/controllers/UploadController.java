package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.services.UploadService;

/**
 * Created by dough on 10/13/2016.
 */
@Controller
public class UploadController {

    /**
     * The service responsible for parsing files and inserting them into the database.
     */
    @Autowired
    private UploadService uploadService;

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
    @RequestMapping(value = "/{deviceKey}", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFile(
            @PathVariable(value = "deviceKey") String deviceKey,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        /*
        TODO refactor this mess into the uploadService. Also, get a CSV parsing library. There is
        no reason the file .isEmpty() check should be done in the controller.
         */
        if (!file.isEmpty()) {
            uploadService.setMultipartFile(file).parseFile();
            return "{status: \"uploaded\"}";
        }
        return "{status: \"failed\"}";
    }

}
