package space.hideaway.services;

import space.hideaway.model.UploadHistory;

import java.util.UUID;


public interface UploadHistoryService {

    /**
     * Save a new uploadHistory object into the database.
     *
     * @param uploadHistory The newly created uploadHistory object.
     */
    void save(UploadHistory uploadHistory);

    UploadHistory setViewed(UUID historyID);
}
