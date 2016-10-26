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

    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/{deviceKey}", method = RequestMethod.POST)
    public @ResponseBody String uploadFile(
            @PathVariable(value = "deviceKey") String deviceKey,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        if (!file.isEmpty()) {
            uploadService.setMultipartFile(file).parseFile();
            return "{status: \"uploaded\"}";
        }
        return "{status: \"failed\"}";
    }

}
