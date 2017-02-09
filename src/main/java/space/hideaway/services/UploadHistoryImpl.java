package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.UploadHistory;
import space.hideaway.model.User;
import space.hideaway.repositories.UploadHistoryRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    public void save(UUID deviceId, int userID, boolean error, long duration, String message, int records)
    {
        UploadHistory uploadHistory = new UploadHistory();
        uploadHistory.setDeviceID(deviceId);
        uploadHistory.setUserID(userID);
        uploadHistory.setError(false);
        uploadHistory.setDateTime(new Date(System.currentTimeMillis()));
        uploadHistory.setDuration(duration);
        uploadHistory.setDescription("Data was uploaded successfully");
        uploadHistory.setRecords(records);

        save(uploadHistory);
    }

    @Override
    public long countByUserID(User currentLoggedInUser)
    {
        return uploadHistoryRepository.countByUserID(Math.toIntExact(currentLoggedInUser.getId()));
    }

    @Override
    public List<UploadHistory> getLastWeek(User user)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date date = calendar.getTime();
        return uploadHistoryRepository.getHistoric(date, Math.toIntExact(user.getId()));
    }

    @Override
    public List<UploadHistory> getLastMonth(User user)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date date = calendar.getTime();
        return uploadHistoryRepository.getHistoric(date, Math.toIntExact(user.getId()));
    }

    @Override
    public List<UploadHistory> getLastYear(User user)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -365);
        Date date = calendar.getTime();
        return uploadHistoryRepository.getHistoric(date, Math.toIntExact(user.getId()));
    }

}
