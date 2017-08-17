package space.hideaway.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.exceptions.UserNotFoundException;
import space.hideaway.model.User;
import space.hideaway.services.UserService;


@Component
public class UserAccountValidator implements Validator
{

    private final UserService userService;

    @Autowired
    public UserAccountValidator(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass)
    {
        return User.class.equals(aClass);
    }


    @Override
    public void validate(Object o, Errors errors)
    {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        User byEmail = userService.findByEmail(user.getEmail());
        if (byEmail != null)
        {
            errors.rejectValue("email", "Duplicate.userForm.user.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");

        if (user.getUsername().length() < 6 || user.getUsername().length() > 32)
        {
            errors.rejectValue("username", "Size.userForm.username");
        }
        try
        {
            User byUsername = userService.findByUsername(user.getUsername());
            if (byUsername != null)
            {
                errors.rejectValue("username", "Duplicate.userForm.user.username");
            }
        } catch (UserNotFoundException ignored) {

        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32)
        {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getConfirmationPassword().equals(user.getPassword()))
        {
            errors.rejectValue("confirmationPassword", "Diff.userForm.passwordConfirm");
        }
    }
}
