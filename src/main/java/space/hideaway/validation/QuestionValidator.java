package space.hideaway.validation;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.model.User;

/**
 * Created by dough on 1/19/2017.
 */
@Component
public class QuestionValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "middleInitial", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");
    }
}
