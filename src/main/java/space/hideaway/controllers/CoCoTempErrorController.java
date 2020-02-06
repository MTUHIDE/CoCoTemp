package space.hideaway.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CoCoTempErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model modelMap) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                modelMap.addAttribute("error","Error Code: "+statusCode+"\n"+"Resource Not Found");
                return "error";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                modelMap.addAttribute("error","Error Code: "+statusCode+"\n"+"Internal Server Error");
                return "error";
            }
        }
        else {
            Integer statusCode = Integer.valueOf(status.toString());
            modelMap.addAttribute("error", "Error Code: " + statusCode + "\n");
        }
        return "error";

    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
