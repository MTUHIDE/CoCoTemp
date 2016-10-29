package space.hideaway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.hideaway.repositories.UserRepository;

/**
 * HIDE CoCoTemp 2016
 * A unused class, but contains an initialize method that runs on startup of the server.
 * SHOULD BE USED FOR TESTING ONLY.
 *
 * @author Piper Dougherty
 */
@Component
public class StartupService {

    @Autowired
    UserRepository userRepository;

    /**
     * This method has no purpose at the moment, but
     * I thought it would be useful to have some method for
     * running simple code at startup. DO NOT USE FOR BUSINESS LOGIC.
     * Any code that would better fit as it's own @Component should be
     * implemented accordingly.
     */
    public void initialize() {

    }

}
