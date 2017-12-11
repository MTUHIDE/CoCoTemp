package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.hideaway.model.Threshold;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;

import java.util.List;
import java.util.UUID;

public interface ThresholdRepository extends JpaRepository<Threshold, Long>
{
    @Query(value = "select * from Threshold where site_id = :siteId and user_id = :userId", nativeQuery = true)
    List<Threshold> getThreshold(@Param("siteId") UUID siteId, @Param("userId") Long userId);
}
