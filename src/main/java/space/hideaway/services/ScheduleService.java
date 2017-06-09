package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.Site;
import space.hideaway.model.User;
import space.hideaway.repositories.DeviceRepository;
import space.hideaway.repositories.SiteRepository;
import space.hideaway.repositories.UserRepository;

/**
 * Created by Justin on 6/5/2017.
 *
 * This class is used to insert test data into the database
 * at startup when the 'local' environment is active.
 *
 */

@Service
@Profile("local")
public class ScheduleService {

    private final UserRepository userRepository;
    private final SiteRepository siteRepository;

    private final DeviceRepository deviceRepository;

    @Autowired
    public ScheduleService(UserRepository userRepository, SiteRepository siteRepository, DeviceRepository deviceRepository){
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Ran once at startup to insert a test user and site in the database.
     * Account username: 'TESTACC' and password: 'password'.
     * Site name: 'Test Site' and is assigned to 'TESTACC' account.
     */
    @Scheduled(fixedRate = Long.MAX_VALUE)
    private void insertData(){
        User user = createUser("TESTACC","password","Test@TEST.com","John",
                "P","Doe", 1);
        Site site = createSite(user, "test",10.1,11.1,"Test Site");
        userRepository.save(user);
        siteRepository.save(site);

        //Testing device table will remove later
        Device device = new Device();
        device.setUserID(user.getId());
        deviceRepository.save(device);

    }

    /**
     * Creates a user for the CoCo Temp website
     * @param username Account's username
     * @param rawPassword Non encrypted password
     * @param email Account's Email
     * @param firstName First name of owner
     * @param lastName Last name of owner
     * @param middleInitial Middle initial
     * @param id User ID
     * @return A user
     */
    private User createUser(String username, String rawPassword, String email,
                            String firstName, String lastName, String middleInitial, long id){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername(username);
        user.setId(id);
        user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setMiddleInitial(middleInitial);
        user.setLastName(lastName);
        return user;
    }

    /**
     * Create a site
     * @param user The user the site is assigned to
     * @param description Information about the site
     * @param latitude The location
     * @param longitude The location
     * @param name The site's name
     * @return A site
     */
    private Site createSite(User user, String description, double latitude, double longitude, String name){
        Site site = new Site();
        site.setUserID(user.getId());
        site.setSiteDescription(description);
        site.setSiteLatitude(latitude);
        site.setSiteLongitude(longitude);
        site.setSiteName(name);
        return site;
    }

}
