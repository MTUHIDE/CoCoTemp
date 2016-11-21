package space.hideaway.services;

import space.hideaway.UserNotFoundException;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.Set;

public interface UserService {

    void save(User user);


    User findByUsername(String username) throws UserNotFoundException;


    Set<Device> getDevices(String username) throws UserNotFoundException;
}
