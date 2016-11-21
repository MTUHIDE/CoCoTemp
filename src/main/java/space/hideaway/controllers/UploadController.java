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
