package space.hideaway.services;

import space.hideaway.model.User;

/**
 * Created by dough on 10/9/2016.
 */
public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
