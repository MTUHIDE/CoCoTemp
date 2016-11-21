package space.hideaway.services;

import space.hideaway.model.UploadHistory;


public interface UploadHistoryService {

    void save(UploadHistory uploadHistory);


    void delete(String id);
}
