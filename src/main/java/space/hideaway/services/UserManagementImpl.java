package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import space.hideaway.UserNotFoundException;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.repositories.RoleRepository;
import space.hideaway.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserManagementImpl implements UserService {


    private final UserRepository userRepository;


    private final RoleRepository roleRepository;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SecurityServiceImplementation securityServiceImplementation;

    @Autowired
    public UserManagementImpl(RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                              SecurityServiceImplementation securityServiceImplementation) {
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.securityServiceImplementation = securityServiceImplementation;
    }


    @Override
    public void save(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        user.setRoleSet(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }


    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("The user was not found in the database.");
        } else {
            return user;
        }
    }

    public User getCurrentLoggedInUser() {
        try {
            return findByUsername(securityServiceImplementation.findLoggedInUsername());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Set<Device> getDevices(String username) throws UserNotFoundException {
        return findByUsername(username).getDeviceSet();
    }
}
