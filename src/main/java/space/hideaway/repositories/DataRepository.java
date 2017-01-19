package space.hideaway.repositories;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import space.hideaway.model.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface DataRepository extends DataTablesRepository<Data, UUID> {

    /**
     * Obtain an average data point for each station on the current day.
     *
     * @return A list of average data points.
     */
    @Query("select new Data(data.id, data.deviceID, data.dateTime, avg(data.temperature)) from Data data where DATE(data.dateTime) = current_date group by data.deviceID")
    List<Data> getAverageDataForCurrentDay();

    /**
     * Select all data points that match the given user ID.
     *
     * @param id The user id to obtain data points for.
     * @return A list of all data points owned by a user.
     */
    @Query("select data from Data data where data.userID = ?1")
    Set<Data> findByUserID(Long id);
}
