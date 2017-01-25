package space.hideaway.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import space.hideaway.model.Data;

import java.util.UUID;


public interface DataRepository extends CrudRepository<Data, UUID>
{
    @Query("SELECT count(d) from Data d where d.userID=:id")
    Long countByUserID(@Param("id") int id);
}
