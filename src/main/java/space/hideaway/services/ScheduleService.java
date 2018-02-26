package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import space.hideaway.model.News;
import space.hideaway.model.Role;
import space.hideaway.model.site.Site;
import space.hideaway.model.User;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.repositories.NewsRepository;
import space.hideaway.repositories.RoleRepository;
import space.hideaway.repositories.site.SiteMetadataRepository;
import space.hideaway.repositories.site.SiteRepository;
import space.hideaway.repositories.UserRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Justin
 * 6/5/2017
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
    private final NewsRepository newsRepository;
    private final RoleRepository roleRepository;
    private final SiteMetadataRepository siteMetadataRepository;

    @Autowired
    public ScheduleService(UserRepository userRepository,
                           SiteRepository siteRepository,
                           NewsRepository newsRepository,
                           RoleRepository roleRepository,
                           SiteMetadataRepository siteMetadataRepository)
    {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.newsRepository = newsRepository;
        this.roleRepository = roleRepository;
        this.siteMetadataRepository = siteMetadataRepository;
    }

    /**
     * Ran once at startup to insert a test user and site in the database.
     * Also adds three news posts.
     * Account username: 'TESTACC' and password: 'password'.
     * Site name: 'Test Site' and is assigned to 'TESTACC' account.
     */
    @Scheduled(fixedRate = Long.MAX_VALUE)
    private void insertData()
    {
        User user = createUser(
                "TESTACC","password",
                "Test@TEST.com","John",
                "Doe","P", 1);
        Role userRole = roleRepository.findByName("ROLE_PUBLIC");
        user.setRoleSet(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);

        User admin = createUser(
                "ADMINACC","password",
                "Admin@TEST.com","John",
                "Doe","P", 2);
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        admin.setRoleSet(new HashSet<Role>(Arrays.asList(adminRole)));
        userRepository.save(admin);


        for (int i = 0; i < 5; i++) {
            double rand = (Math.random()-.5)*13;
            double rand2 = (Math.random()-.5)*13;
            Site site = createSite(
                    user, "A site for local testing.",
                    37 + rand2,-95 + rand,"Test Site" + i);
            siteRepository.save(site);
            String enviroment = (i%2 == 1) ? "urban" : "natural";
            String canopyType = (i%2 == 1) ? "No canopy" : "Metal roof";
            SiteMetadata metadata = createSiteMetadata(site, enviroment,canopyType, i%2+5);
            siteMetadataRepository.save(metadata);
        }

        for (int i = 0; i < 3; i++) {
            News news = createNewsPost("Local test post. Created at: " + System.currentTimeMillis() + "ms",
                    "Test Post " + i);
            newsRepository.save(news);
        }

    }

    /**
     * Creates a news post
     *
     * @param content The text body of the news post
     * @param title The title of the news post
     * @return A news post
     */
    private News createNewsPost(String content, String title)
    {
        News news = new News();
        news.setContent(content);
        news.setTitle(title);
        news.setDateTime(new Date());
        return news;
    }

    /**
     * Creates a user for the CoCo Temp website
     *
     * @param username Account's username
     * @param rawPassword Non encrypted password
     * @param email Account's Email
     * @param firstName First name of owner
     * @param lastName Last name of owner
     * @param middleInitial Middle initial
     * @param id User ID
     * @return A user
     */
    private User createUser(
            String username, String rawPassword, String email,
            String firstName, String lastName, String middleInitial, long id)
    {
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
     *
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

    private SiteMetadata createSiteMetadata(Site site, String environment, String canopyType, double groundElevation){
        SiteMetadata siteMetadata = new SiteMetadata();
        siteMetadata.setEnvironment(environment);
        siteMetadata.setSiteID(site.getId());
        siteMetadata.setCanopyType(canopyType);
        siteMetadata.setGroundElevation(groundElevation);
        return siteMetadata;
    }

}
