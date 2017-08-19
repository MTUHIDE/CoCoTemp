package space.hideaway.validation;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.model.site.Site;

/**
 * Created by dough on 2017-02-09.
 */
@Component
public class SiteQuestionnaireValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return Site.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "siteDescription", "Site.emptyDescription");
    }
}

