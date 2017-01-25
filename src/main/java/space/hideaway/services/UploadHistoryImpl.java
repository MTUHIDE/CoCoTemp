package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.UploadHistory;
import space.hideaway.model.User;
import space.hideaway.repositories.UploadHistoryRepository;

import java.util.Date;
import java.util.UUID;


@Service
public class UploadHistoryImpl implements UploadHistoryService {

    private final UploadHistoryRepository uploadHistoryRepository;

    @Autowired
    public UploadHistoryImpl(UploadHistoryRepository uploadHistoryRepository) {
        this.uploadHistoryRepository = uploadHistoryRepository;
    }

    /**
     * Save a new uploadHistory object into the database.
     *
     * @param uploadHistory The newly created uploadHistory object.
     */
    @Override
    public void save(UploadHistory uploadHistory) {
        uploadHistoryRepository.save(uploadHistory);
    }

    /**
     * Delete an uploadHistory object from the database by ID.
     *
     * @param id The ID of the uploadHistory record in the database.
     */
    @Override
    public UploadHistory setViewed(UUID id) {


        UploadHistory one = uploadHistoryRepository.findOne(id);
        one.setViewed(true);
        uploadHistoryRepository.save(one);
        return one;
    }

    @Override
    public void save(UUID deviceId, boolean error, long duration, String message)
    {
        UploadHistory uploadHistory = new UploadHistory();
        uploadHistory.setDeviceID(deviceId);
        uploadHistory.setError(false);
        uploadHistory.setDateTime(new Date(System.currentTimeMillis()));
        uploadHistory.setDuration(duration);
        uploadHistory.setDescription("Data was uploaded successfully");
        save(uploadHistory);
    }

    @Override
    public long countByUserID(User currentLoggedInUser)
    {
        return uploadHistoryRepository.countByUserID(Math.toIntExact(currentLoggedInUser.getId()));
    }

}
