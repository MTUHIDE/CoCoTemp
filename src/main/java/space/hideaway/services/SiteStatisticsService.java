package space.hideaway.services;

import space.hideaway.model.Site;
import space.hideaway.model.SiteStatistics;

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
