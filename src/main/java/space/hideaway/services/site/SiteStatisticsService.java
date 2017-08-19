package space.hideaway.services.site;

import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;

import java.util.concurrent.Future;

/**
 * Created by dough on 2017-02-19.
 */
public interface SiteStatisticsService
{
    Future<SiteStatistics> recalculateStatistics(Site site);

    SiteStatistics save(SiteStatistics siteStatistics);

    SiteStatistics getMostRecent(Site site);
}
