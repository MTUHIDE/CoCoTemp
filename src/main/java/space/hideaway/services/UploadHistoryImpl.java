package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.UploadHistory;
import space.hideaway.repositories.UploadHistoryRepository;

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

}
