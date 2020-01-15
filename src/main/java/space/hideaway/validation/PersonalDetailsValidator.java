package space.hideaway.validation;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.model.User;

/**
 * Created by dough
 * 1/19/2017
 *
 * Validates a user's first, middle, and last names are not empty.
 */
@Component
public class PersonalDetailsValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return User.class.equals(clazz);
    }

    /**
     * Validates a user's first, middle, and last names are not empty.
     *
     * @param target the user
     * @param errors the bindingResult
     */
    @Override
    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "middleInitial", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"tempStandard","NotEmpty");
    }
}
