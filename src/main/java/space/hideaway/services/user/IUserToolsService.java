package space.hideaway.services.user;

import space.hideaway.model.User;

public interface IUserToolsService {

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

}