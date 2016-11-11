package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.hideaway.model.Data;

import java.util.List;
import java.util.UUID;

/**
 * Created by dough on 11/1/2016.
 */
public interface DataRepository extends JpaRepository<Data, UUID> {
    @Query("select new Data(data.id, data.deviceID, data.dateTime, avg(data.temperature)) from Data data where DATE(data.dateTime) = current_date group by data.deviceID")
    List<Data> getAverageDataForCurrentDay();
}
