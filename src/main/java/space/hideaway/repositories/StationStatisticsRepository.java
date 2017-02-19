package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.StationStatistics;

import java.util.UUID;

/**
 * Created by dough on 2017-02-19.
 */
public interface StationStatisticsRepository extends JpaRepository<StationStatistics, UUID>
{
}
