package space.hideaway.services;

/**
 * Created by dough on 10/9/2016.
 */
public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
