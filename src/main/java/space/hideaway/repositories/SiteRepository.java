package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.hideaway.model.Site;

import java.util.UUID;

public interface SiteRepository extends JpaRepository<Site, UUID>
{
    @Query("SELECT count(d) from Site d where d.userID=:id")
    Long countByUserID(@Param("id") Long id);
}
