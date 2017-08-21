package space.hideaway.services.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.upload.UploadHistory;
import space.hideaway.model.User;
import space.hideaway.repositories.UploadHistoryRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UploadHistoryServiceImplementation implements UploadHistoryService
{

    private final UploadHistoryRepository uploadHistoryRepository;

    @Autowired
    public UploadHistoryServiceImplementation(UploadHistoryRepository uploadHistoryRepository)
    {
        this.uploadHistoryRepository = uploadHistoryRepository;
    }

    /**
     * Save a new uploadHistory object into the database.
     *
     * @param uploadHistory The newly created uploadHistory object.
     * @return the uploadHistory object
     */
    @Override
    public UploadHistory save(UploadHistory uploadHistory)
    {
        return uploadHistoryRepository.save(uploadHistory);
    }

    /**
     * Sets viewed to true for an uploadHistory object.
     *
     * @param id The ID of the uploadHistory record in the database.
     * @return the uploadHistory object
     */
    @Override
    public UploadHistory setViewed(UUID id)
    {
        UploadHistory one = uploadHistoryRepository.getOne(id);
        one.setViewed(true);
        uploadHistoryRepository.save(one);
        return one;
    }

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
    @Override
    public UploadHistory savePending(
            UUID siteId,
            int userID,
            boolean error,
            long duration,
            String message,
            int records)
    {
        UploadHistory uploadHistory = new UploadHistory();
        uploadHistory.setDateTime(new Date(System.currentTimeMillis()));
        uploadHistory.setSiteID(siteId);
        uploadHistory.setUserID(userID);
        uploadHistory.setError(error);
        uploadHistory.setDuration(duration);
        uploadHistory.setDescription(message);
        uploadHistory.setRecords(records);
        return save(uploadHistory);
    }

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
    @Override
    public UploadHistory saveFinished(
            UploadHistory uploadHistory,
            boolean error,
            long duration,
            int size,
            String message)
    {
        uploadHistory.setError(error);
        uploadHistory.setDuration(duration);
        uploadHistory.setDescription(message);
        uploadHistory.setRecords(size);
        return save(uploadHistory);
    }

    /**
     * Gets the count of uploads by an user.
     *
     * @param currentLoggedInUser The user id of the currently login user
     * @return The number of uploads.
     */
    @Override
    public long countByUserID(User currentLoggedInUser)
    {
        return uploadHistoryRepository.countByUserID(Math.toIntExact(currentLoggedInUser.getId()));
    }

    /**
     * Gets all uploads by a user in the last week.
     *
     * @param user The user
     * @return All uploads in the last week
     */
    @Override
    public List<UploadHistory> getLastWeek(User user)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date date = calendar.getTime();
        return uploadHistoryRepository.getHistoric(date, Math.toIntExact(user.getId()));
    }

    /**
     * Gets all uploads by a user in the last month.
     *
     * @param user The user
     * @return All uploads in the last month
     */
    @Override
    public List<UploadHistory> getLastMonth(User user)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date date = calendar.getTime();
        return uploadHistoryRepository.getHistoric(date, Math.toIntExact(user.getId()));
    }

    /**
     * Gets all uploads by a user in the last year.
     *
     * @param user The user
     * @return All uploads in the last year
     */
    @Override
    public List<UploadHistory> getLastYear(User user)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -365);
        Date date = calendar.getTime();
        return uploadHistoryRepository.getHistoric(date, Math.toIntExact(user.getId()));
    }

}
