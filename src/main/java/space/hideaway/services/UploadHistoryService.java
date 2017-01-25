package space.hideaway.services;

import space.hideaway.model.UploadHistory;
import space.hideaway.model.User;

import java.util.UUID;


public interface UploadHistoryService {

    /**
     * Save a new uploadHistory object into the database.
     *
     * @param uploadHistory The newly created uploadHistory object.
     */
    void save(UploadHistory uploadHistory);

    UploadHistory setViewed(UUID historyID);

    void save(UUID deviceId, boolean error, long duration, String message);

    long countByUserID(User currentLoggedInUser);
}
