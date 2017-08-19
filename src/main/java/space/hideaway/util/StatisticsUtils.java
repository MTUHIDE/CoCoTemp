package space.hideaway.util;

import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;

import java.util.List;

/**
 * Created by dough on 2017-03-02.
 */
public class StatisticsUtils
{
    public static double getMostRecentAllMean(Site site)
    {
        List<SiteStatistics> sortMostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        return sortMostRecentFirst.isEmpty() ? 0 : sortMostRecentFirst.get(0).getAllAvg();
    }

    public static double getMostRecentAllMax(Site site)
    {
        List<SiteStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        return mostRecentFirst.isEmpty() ? 0 : mostRecentFirst.get(0).getAllMax();
    }

    public static double getMostRecentAllMin(Site site)
    {
        List<SiteStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        return mostRecentFirst.isEmpty() ? 0 : mostRecentFirst.get(0).getAllMin();
    }

    public static double getMostRecentAllDeviation(Site site)
    {
        List<SiteStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        return mostRecentFirst.isEmpty() ? 0 : mostRecentFirst.get(0).getAllDeviation();
    }
}
