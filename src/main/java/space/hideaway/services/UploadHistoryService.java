package space.hideaway.services;

import space.hideaway.model.UploadHistory;

/**
 * Created by dough on 11/20/2016.
 */
public interface UploadHistoryService {
    /**
     * Insert a new history record into the database.
     *
     * @param uploadHistory
     */
    void save(UploadHistory uploadHistory);

    /**
     * Delete a record from the database by id.
     *
     * @param id The id of the history object to delete.
     */
    void delete(String id);
}
