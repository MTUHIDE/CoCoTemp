package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.SiteStatistics;

import java.util.UUID;

/**
 * Created by dough on 2017-02-19.
 */
public interface StationStatisticsRepository extends JpaRepository<SiteStatistics, UUID>
{
}
