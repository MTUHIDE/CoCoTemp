package space.hideaway.services;

import space.hideaway.model.UploadHistory;


public interface UploadHistoryService {

    /**
     * Save a new uploadHistory object into the database.
     *
     * @param uploadHistory The newly created uploadHistory object.
     */
    void save(UploadHistory uploadHistory);

    /**
     * Delete an uploadHistory object from the database by ID.
     *
     * @param id The ID of the uploadHistory record in the database.
     */
    void delete(String id);
}
