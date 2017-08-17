package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.hideaway.model.UploadHistory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface UploadHistoryRepository extends JpaRepository<UploadHistory, UUID>
{
    @Query("SELECT count(u) from UploadHistory u where u.userID=:id")
    long countByUserID(@Param("id") int id);

    @Query("select u from UploadHistory u where u.dateTime > :previousDate and u.userID=:id order by u.dateTime asc ")
    List<UploadHistory> getHistoric(@Param("previousDate") Date previousDate, @Param("id") int id);
}
