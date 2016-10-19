package space.hideaway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.hideaway.model.Device;
import space.hideaway.repositories.UserRepository;

/**
 * Created by dough on 10/11/2016.
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
