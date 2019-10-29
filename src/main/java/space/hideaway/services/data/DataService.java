package space.hideaway.services.data;

import space.hideaway.model.Data;
import space.hideaway.model.site.Site;
import space.hideaway.model.User;
import space.hideaway.util.HistoryUnit;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface DataService
{
    /**
     * Save a new data point into the database.
     *
     * @param data The new data point to be saved.
     */
    void save(Site site, Data data);

    /**
     * Batch save a large amount of data points.
     *
     * @param dataList A list of points to be saved.
     * @return The newly saved list.
     */
    List<Data> batchSave(Site site, List<Data> dataList);

    /**
     * Gets the count of records uploaded by an user.
     *
     * @param currentLoggedInUser The user id of the current login user
     * @return The number of records.
     */
    Long countByUserID(User currentLoggedInUser);

    /**
     *  Gets records for a site between two points in time.
     *
     * @param time Starting point in time
     * @param site The site
     * @return All of the records between now and <code>time</code>
     */
    List<Data> getHistoric(HistoryUnit time, Site site);


    Data findIfDataExistsAlready(UUID SiteID, Date dateTime, int userID, double temperature);
}
