package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.UploadHistory;
import space.hideaway.repositories.UploadHistoryRepository;

import java.util.UUID;


@Service
public class UploadHistoryImpl implements UploadHistoryService {
    @Autowired
    UploadHistoryRepository uploadHistoryRepository;

    @Override
    public void save(UploadHistory uploadHistory) {
        uploadHistoryRepository.save(uploadHistory);
    }

    @Override
    public void delete(String id) {
        uploadHistoryRepository.delete(UUID.fromString(id));
    }

}
