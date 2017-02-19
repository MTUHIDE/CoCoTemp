package space.hideaway.services;

import space.hideaway.model.Device;
import space.hideaway.model.StationStatistics;

import java.util.concurrent.Future;

/**
 * Created by dough on 2017-02-19.
 */
public interface StationStatisticsService
{
    Future<StationStatistics> recalculateStatistics(Device device);

    StationStatistics save(StationStatistics stationStatistics);

    StationStatistics getMostRecent(Device device);
}
