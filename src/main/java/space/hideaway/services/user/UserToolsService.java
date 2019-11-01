package space.hideaway.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.User;
import space.hideaway.model.security.PasswordResetToken;
import space.hideaway.repositories.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserToolsService implements IUserToolsService {

    @Autowired
    private UserRepository userRepository;

    public UserToolsService() {
        super();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        final PasswordResetToken userToken = new PasswordResetToken(user, token);
    }
}
