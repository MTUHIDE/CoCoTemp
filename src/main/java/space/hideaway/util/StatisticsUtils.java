package space.hideaway.util;

import space.hideaway.model.Device;
import space.hideaway.model.StationStatistics;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by dough on 2017-03-02.
 */
public class StatisticsUtils
{
    public static double getMostRecentAllMean(Device device)
    {
        List<StationStatistics> sortMostRecentFirst = SortingUtils.sortMostRecentFirst(device.getStationStatisticsList
                ());
        return sortMostRecentFirst.isEmpty() ? 0 : Double.parseDouble(new DecimalFormat("#.##").format(sortMostRecentFirst.get(0).getAllAvg()));
    }

    public static double getMostRecentAllMax(Device device)
    {
        List<StationStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(device.getStationStatisticsList
                ());
        return mostRecentFirst.isEmpty() ? 0 : Double.parseDouble(new DecimalFormat("#.##").format(mostRecentFirst.get(0).getAllMax()));
    }

    public static double getMostRecentAllMin(Device device)
    {
        List<StationStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(device.getStationStatisticsList
                ());
        return mostRecentFirst.isEmpty() ? 0 : Double.parseDouble( new DecimalFormat("#.##").format(mostRecentFirst.get(0).getAllMin()));
    }

    public static double getMostRecentAllDeviation(Device device)
    {
        List<StationStatistics> mostRecentFirst = SortingUtils.sortMostRecentFirst(device.getStationStatisticsList
                ());
        return mostRecentFirst.isEmpty() ? 0 : Double.parseDouble(new DecimalFormat("#.##").format(mostRecentFirst.get(0).getAllDeviation()));
    }
}
