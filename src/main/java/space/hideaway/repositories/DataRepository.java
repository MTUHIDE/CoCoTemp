package space.hideaway.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import space.hideaway.model.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface DataRepository extends CrudRepository<Data, UUID>
{
    @Query("SELECT count(d) from Data d where d.userID=:id")
    Long countByUserID(@Param("id") int id);

    @Query("select d from Data d where d.dateTime > :previousDate and d.siteID=:id order by d.dateTime asc ")
    List<Data> getHistoric(@Param("previousDate") Date previousDate, @Param("id") UUID siteID);
}
