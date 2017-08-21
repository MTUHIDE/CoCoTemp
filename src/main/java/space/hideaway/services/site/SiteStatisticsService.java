package space.hideaway.services.site;

import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;

import java.util.concurrent.Future;

/**
 * Created by dough
 * 2017-02-19
 */
public interface SiteStatisticsService
{
    /**
     * Calculates the statistics for a site. This should be called after new
     * records have been added to the site. Statistics are calculated from a
     * pool of available threads, so not to slow down the server.
     *
     * @param site The site to have statistics recalculated
     * @return The recalculated statistics
     */
    Future<SiteStatistics> recalculateStatistics(Site site);

    /**
     * Saves site statistics into the database.
     *
     * @param siteStatistics The site statistics to save
     * @return The saved site statistics
     */
    SiteStatistics save(SiteStatistics siteStatistics);

    /**
     * Gets the most recent statistics for a site. If the site has no statistics
     * an 'empty' siteStatistics object is return with all statistics set to zero.
     *
     * @param site The site that statistics are being requested
     * @return The statistics
     */
    SiteStatistics getMostRecent(Site site);
}
