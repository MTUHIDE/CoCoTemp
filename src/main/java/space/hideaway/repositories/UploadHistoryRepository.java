package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import space.hideaway.model.upload.UploadHistory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface UploadHistoryRepository extends JpaRepository<UploadHistory, UUID>
{
    /**
     * Gets the count of uploads by an user.
     *
     * @param id The user id
     * @return The number of uploads.
     */
    @Query("SELECT count(u) from UploadHistory u where u.userID=:id")
    long countByUserID(@Param("id") int id);

    /**
     * Gets upload records between two points in time.
     *
     * @param previousDate Starting point in time
     * @param id The user id
     * @return All of the uploads between now and <code>previousDate</code>
     */
    @Query("select u from UploadHistory u where u.dateTime > :previousDate and u.userID=:id order by u.dateTime asc ")
    List<UploadHistory> getHistoric(@Param("previousDate") Date previousDate, @Param("id") int id);


}
