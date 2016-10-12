package space.hideaway.services;

import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.Set;

/**
 * Created by dough on 10/9/2016.
 */
public interface UserService {
    void save(User user);
    User findByUsername(String username);

    Set<Device> getDevices(String username);
}
