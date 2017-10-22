package space.hideaway.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.model.site.Site;

/**
 * Created by dough
 * 2017-02-09
 *
 * Edited by Justin Havely
 * 2017-21-08
 *
 * Validates a site in two parts. Part one being the name and part two being the description.
 */
@Component
public class SiteValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return Site.class.equals(clazz);
    }

    /**
     * Validates the site's name to be less than 27 characters long.
     *
     * @param target the site
     * @param errors the bindingResult
     */
    @Override
    public void validate(Object target, Errors errors)
    {
        Site site = (Site) target;
        if (site.getSiteName().length() > 27)
        {
            errors.rejectValue("siteName", "Site.longName");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "siteName", "Site.emptyName");
    }

    /**
     * Validates the site's description to be filled.
     *
     * @param site the site
     * @param errors the bindingResult
     */
    public void validateDescription(Site site, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "siteDescription", "Site.emptyDescription");
    }
}
