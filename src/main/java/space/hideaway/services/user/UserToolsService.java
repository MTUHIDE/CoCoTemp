package space.hideaway.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.User;
import space.hideaway.model.security.PasswordResetToken;
import space.hideaway.model.security.VerificationToken;
import space.hideaway.repositories.UserRepository;
import space.hideaway.repositories.VerificationTokenRepository;

import javax.transaction.Transactional;

@Service
public class UserToolsService implements IUserToolsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public UserToolsService() {
        super();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        final PasswordResetToken userToken = new PasswordResetToken(user, token);
    }
    public void createVerificationTokenForUser(User user,String token){
        final VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }
    public VerificationToken getVerificationToken(String verificationToken){
        return verificationTokenRepository.findByToken(verificationToken);
    }
    public void deleteVerificationToken(VerificationToken verificationToken)
    {

        verificationTokenRepository.delete(verificationToken);
    }
}
