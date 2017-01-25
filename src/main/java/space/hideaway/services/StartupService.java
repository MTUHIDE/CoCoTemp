package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.hideaway.repositories.UserRepository;


@Component
public class StartupService {

    private final
    UserRepository userRepository;

    @Autowired
    public StartupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void initialize() {

    }

}
