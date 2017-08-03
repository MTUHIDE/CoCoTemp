package space.hideaway.util;

import space.hideaway.model.Site;
import space.hideaway.model.SiteStatistics;

import java.util.List;

/**
 * Created by dough on 2017-03-02.
 */
public class StatisticsUtils {
    public static double getMostRecentAllMean(Site site) {
        List<SiteStatistics> sortMostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        double avg = sortMostRecentFirst.isEmpty() ? 0 : sortMostRecentFirst.get(0).getAllAvg();
        return avg; //Always returns 0 if not saved into avg variable (Bug with JDK 1.8.0.131)
    }

    public static double getMostRecentAllMax(Site site) {
        List<SiteStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        double max = mostRecentFirst.isEmpty() ? 0 : mostRecentFirst.get(0).getAllMax();
        return max; //Always returns 0 if not saved into max variable (Bug with JDK 1.8.0.131)
    }

    public static double getMostRecentAllMin(Site site) {
        List<SiteStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        double min = mostRecentFirst.isEmpty() ? 0 : mostRecentFirst.get(0).getAllMin();
        return min; //Always returns 0 if not saved into min variable (Bug with JDK 1.8.0.131)
    }

    public static double getMostRecentAllDeviation(Site site) {
        List<SiteStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        double stdDev = mostRecentFirst.isEmpty() ? 0 : mostRecentFirst.get(0).getAllDeviation();
        return stdDev; //Always returns 0 if not saved into stdDev variable (Bug with JDK 1.8.0.131)
    }
}
