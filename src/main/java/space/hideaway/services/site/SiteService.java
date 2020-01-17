package space.hideaway.services.site;

import org.springframework.stereotype.Service;
import space.hideaway.model.site.Site;
import space.hideaway.model.User;

import java.util.List;

@Service
public interface SiteService
{

    /**
     * Save a new site into the database.
     *
     * @param site The new site to be inserted.
     */
    Site save(Site site);

    /**
     * Find a site by site ID.
     *
     * @param siteID The ID of the site to obtain.
     * @return The obtained site matching the given siteID.
     */
    Site findByKey(String siteID);

    /**
     * Obtain a list of all sites.
     *
     * @return A list of all sites.
     */
    List<Site> getAllSites();

    /**
     * Compare a siteID and a user, and determine whether the relationship between
     * them is valid.
     *
     * @param user     The user to compare to the site.
     * @param siteID The ID of the site to compare to the user.
     * @return True if the user owns the site, false otherwise.
     */
    boolean isCorrectUser(User user, String siteID);

    /**
     * Compare a siteID and the currently logged in user, and determine whether the relationship between
     * them is valid.
     *
     * @return True if the user owns the site, false otherwise.
     */
    boolean isCorrectUser(String siteKey);

    /**
     * Gets the count of sites created by an user.
     *
     * @param currentLoggedInUser The user id of the current login user
     * @return The number of sites.
     */
    Long countByUserID(User currentLoggedInUser);
}
