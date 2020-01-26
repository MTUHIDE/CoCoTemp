package space.hideaway.controllers.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import space.hideaway.model.News;
import space.hideaway.model.User;
import space.hideaway.services.NewsService;
import space.hideaway.services.user.UserServiceImplementation;
import space.hideaway.validation.PersonalDetailsValidator;

@Controller
public class AdminSettingsController {
    private final UserServiceImplementation userManagement;
    private final PersonalDetailsValidator personalDetailsValidator;
    private final NewsService  newsService;

    @Autowired
    public AdminSettingsController(
            UserServiceImplementation userManagement,
            PersonalDetailsValidator personalDetailsValidator,
            NewsService newsService)
    {
        this.userManagement = userManagement;
        this.personalDetailsValidator = personalDetailsValidator;
        this.newsService = newsService;
    }


    /**
     * The controller method for the settings page.
     *
     * @param model The model maintained by Spring for the settings page.
     * @return The path to the general page template of the settings group.
     */
    @RequestMapping(value = "/adminsettings")
    public String showSettings(Model model)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("news",newsService.getAll());
        model.addAttribute("user", currentLoggedInUser);

        return "adminsettings/admin-general";
    }

    @RequestMapping(value = "/adminsettings/news", params = {"newsID"})
    public String loadNews(Model model, @RequestParam("newsID") long newsID)
    {

        if (!model.containsAttribute("newspost"))
        {
            model.addAttribute("newspost", newsService.findByID(newsID));
        }
        model.addAttribute("news", newsService.getAll());

        return "adminsettings/news";
    }

    @RequestMapping(value = "/adminsettings/news",params = {"update"},method = RequestMethod.POST)
    public String updateNews(
            @ModelAttribute("newspost") News news,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {

        if (bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("newpost", news);
            return "redirect:/adminsettings/news?newsID=" + news.getId();
        }

        newsService.save(news);

        return "redirect:/adminsettings/news?newsID=" + news.getId();
    }

    @RequestMapping(value = "/adminsettings/news",params = {"delete"},method = RequestMethod.POST)
    public String deleteNews(
            @ModelAttribute("newspost") News news,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {


        newsService.delete(news);

        return "redirect:/adminsettings/";
    }

    /**
     * The controller mapping to update a user's properties via a post request from.
     *
     * @param model The model maintained by Spring for the site settings page.
     * @param user The user that has been edited.
     * @param bindingResult The module for linking validation errors to the template.
     * @return Redirect command to the general settings page
     */
    @RequestMapping(value = "/adminsettings/update", method = RequestMethod.POST)
    public String updateGeneral(
            Model model,
            @ModelAttribute("user") User user,
            BindingResult bindingResult)
    {
        // Validates first, middle, and last name fields.
        personalDetailsValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
            return "adminsettings/admin-general";
        }

        userManagement.update(user);
        return "redirect:/adminsettings";
    }
}
