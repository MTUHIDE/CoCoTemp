package space.hideaway.repositories.site;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.site.SiteStatistics;

import java.util.UUID;

/**
 * Created by dough on 2017-02-19.
 */
public interface SiteStatisticsRepository extends JpaRepository<SiteStatistics, UUID>
{
}
