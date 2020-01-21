package space.hideaway.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.exceptions.UserNotFoundException;
import space.hideaway.model.User;
import space.hideaway.services.user.UserService;

/**
 * Validates a new account.
 */
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

    /**
     *Checks to see if email does not exist.
     *
     * @param o the given user
     * @param errors the bindingResult
     * */
    public void validateEmail(Object o, Errors errors){
        User userTemp = (User) o;//This tempUser serves to hold the email given from the forgotPassword page.

        User user = userService.findByEmail(userTemp.getEmail());//Finds user based on given email
        //If the given user is null then the email does not exist
        if(user == null){
            errors.rejectValue("email", "userForm.user.email", "This email does not exist");
        } else {
            // Email does not exist
            User byEmail = userService.findByEmail(user.getEmail());
            if (byEmail == null) {
                errors.rejectValue("email", "userForm.user.email", "This email does not exist");
            }
        }
    }

    /**
     * Validates a new account. Checks for a correct email, username, and password.
     *
     * @param o the new user
     * @param errors the bindingResult
     */
    @Override
    public void validate(Object o, Errors errors)
    {
        User user = (User) o;

        // Email is not empty or a duplicate.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        User byEmail = userService.findByEmail(user.getEmail());
        if (byEmail != null)
        {
            errors.rejectValue("email", "Duplicate.userForm.user.email");
        }

        // Username is between 6 and 27, or a duplicate.
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

        // Password is between 8 and 32, or matches.
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
