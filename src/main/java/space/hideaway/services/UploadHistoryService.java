package space.hideaway.services;

import space.hideaway.model.UploadHistory;
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

    UploadHistory setViewed(UUID historyID);

    UploadHistory savePending(UUID deviceId, int userID, boolean error, long duration, String message, int records);

    UploadHistory saveFinished(UploadHistory uploadHistory, boolean error, long duration, int size, String message);

    long countByUserID(User currentLoggedInUser);

    List<UploadHistory> getLastWeek(User user);

    List<UploadHistory> getLastMonth(User user);

    List<UploadHistory> getLastYear(User user);
}
