package space.hideaway.repositories;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import space.hideaway.model.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by dough on 11/1/2016.
 */
public interface DataRepository extends DataTablesRepository<Data, UUID> {
    @Query("select new Data(data.id, data.deviceID, data.dateTime, avg(data.temperature)) from Data data where DATE(data.dateTime) = current_date group by data.deviceID")
    List<Data> getAverageDataForCurrentDay();


    //TODO MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    @Query("select data from Data data where data.userID = ?1")
    Set<Data> findByUserID(Long id);
}
