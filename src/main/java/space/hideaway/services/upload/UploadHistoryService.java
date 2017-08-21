package space.hideaway.services.upload;

import space.hideaway.model.upload.UploadHistory;
import space.hideaway.model.User;

import java.util.List;
import java.util.UUID;


public interface UploadHistoryService {

    /**
     * Save a new uploadHistory object into the database.
     *
     * @param uploadHistory The newly created uploadHistory object.
     */
    UploadHistory save(UploadHistory uploadHistory);

    /**
     * Sets viewed to true for an uploadHistory object.
     *
     * @param id The ID of the uploadHistory record in the database.
     * @return the uploadHistory object
     */
    UploadHistory setViewed(UUID id);

    /**
     * Saves information about an upload before it has been completely processed.
     * Once the upload is processed <code>saveFinished</code> should be called to
     * update the upload history record.
     *
     * @param siteId The id of the site where the data was collected
     * @param userID The id of the user who uploaded the data
     * @param error If an error has occurred during the upload process
     * @param duration The time the server took to process the upload
     * @param message The upload notes
     * @param records The data being uploaded
     * @return the upload history object
     */
    UploadHistory savePending(UUID siteId, int userID, boolean error, long duration, String message, int records);

    /**
     * Saves information about an upload after it has been completely processed.
     *
     * @param uploadHistory the upload history object
     * @param error if an error has occurred during the upload process
     * @param duration The time the server took to process the upload
     * @param size the number of data records uploaded
     * @param message the upload notes
     * @return the upload history object
     */
    UploadHistory saveFinished(UploadHistory uploadHistory, boolean error, long duration, int size, String message);

    /**
     * Gets the count of uploads by an user.
     *
     * @param currentLoggedInUser The user id of the currently login user
     * @return The number of uploads.
     */
    long countByUserID(User currentLoggedInUser);

    /**
     * Gets all uploads by a user in the last week.
     *
     * @param user The user
     * @return All uploads in the last week
     */
    List<UploadHistory> getLastWeek(User user);

    /**
     * Gets all uploads by a user in the last month.
     *
     * @param user The user
     * @return All uploads in the last month
     */
    List<UploadHistory> getLastMonth(User user);

    /**
     * Gets all uploads by a user in the last year.
     *
     * @param user The user
     * @return All uploads in the last year
     */
    List<UploadHistory> getLastYear(User user);
}
