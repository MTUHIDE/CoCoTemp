package space.hideaway.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import space.hideaway.model.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface DataRepository extends CrudRepository<Data, UUID>
{
    /**
     * Gets the count of records uploaded by an user.
     *
     * @param id The user id
     * @return The number of records.
     */
    @Query("SELECT count(d) from Data d where d.userID=:id")
    Long countByUserID(@Param("id") int id);

    /**
     * Gets records for a site between two points in time.
     *
     * @param previousDate Starting point in time
     * @param siteID The site id
     * @return All of the records between now and <code>previousDate</code>
     */
    @Query("select d from Data d where d.dateTime > :previousDate and d.siteID=:id order by d.dateTime asc ")
    List<Data> getHistoric(@Param("previousDate") Date previousDate, @Param("id") UUID siteID);

    Data findBySiteIDAndDateTimeAndUserIDAndTemperature(UUID siteID, Date dateTime, int userID,double temperature);
}
