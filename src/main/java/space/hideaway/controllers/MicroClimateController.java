package space.hideaway.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.model.User;
import space.hideaway.services.user.UserService;

@Controller
public class MicroClimateController {
    @Autowired
    private Environment env;

    private final UserService userService;

    @Autowired
    public MicroClimateController(UserService userService){
        this.userService=userService;
    }

    @RequestMapping(value = "/microclimate")
    public String showMicroclimate(Model model){

        User user = userService.getCurrentLoggedInUser();
        char tempStandard = 'F';
        if(user!=null) {

            tempStandard=user.getTempStandard();
        }
        model.addAttribute("tempstandard",tempStandard);
        model.addAttribute("NOAAToken",env.getProperty("spring.NOAA.token"));

        return "microclimate";
    }


}
