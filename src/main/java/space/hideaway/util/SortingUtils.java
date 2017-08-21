package space.hideaway.util;

import space.hideaway.model.Data;
import space.hideaway.model.site.SiteStatistics;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dough
 * 2017-02-19
 *
 * Used for sorting lists of data.
 */
public class SortingUtils
{
    /**
     * Sorts the temperature records from newest to oldest.
     *
     * @param dataList The list of temperature records
     * @return The order list of temperature records
     */
    public static List<Data> sortMostRecentFirst(Set<Data> dataList)
    {
        return dataList.stream().sorted(Comparator.comparing(Data::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sorts statistic records from newest to oldest.
     *
     * @param siteStatisticsList The list of statistic records
     * @return The order list of statistic records
     */
    public static List<SiteStatistics> sortMostRecentFirst(List<SiteStatistics> siteStatisticsList)
    {
        return siteStatisticsList.stream().sorted(Comparator.comparing(SiteStatistics::getDate).reversed())
                .collect(Collectors.toList());
    }
}
