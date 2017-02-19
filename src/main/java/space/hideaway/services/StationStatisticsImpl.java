package space.hideaway.services;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.StationStatistics;
import space.hideaway.repositories.StationStatisticsRepository;
import space.hideaway.util.HistoryUnit;
import space.hideaway.util.SortingUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by dough on 2017-02-19.
 */
@Service
public class StationStatisticsImpl implements StationStatisticsService
{
    @Autowired
    StationStatisticsRepository stationStatisticsRepository;

    @Autowired
    DataService dataService;

    @Override
    public Future<StationStatistics> recalculateStatistics(Device device)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(new StatisticsCalculationRunnable(device));
    }

    @Override
    public StationStatistics save(StationStatistics stationStatistics)
    {
        return stationStatisticsRepository.save(stationStatistics);
    }

    @Override
    public StationStatistics getMostRecent(Device device)
    {
        List<StationStatistics> stationStatisticss = SortingUtils.sortMostRecentFirst(device.getStationStatisticsList
                ());
        if (stationStatisticss.isEmpty())
        {
            return StationStatistics.EMPTY_STATISTIC;
        } else
        {
            return stationStatisticss.get(0);
        }
    }

    private class StatisticsCalculationRunnable implements Callable<StationStatistics>
    {

        Device device;
        List<Data> weekData;
        List<Data> monthData;
        List<Data> yearData;
        List<Data> allData;

        StatisticsCalculationRunnable(Device device)
        {
            this.device = device;
        }

        @Override
        public StationStatistics call() throws Exception
        {
            weekData = dataService.getHistoric(HistoryUnit.WEEK, device);
            monthData = dataService.getHistoric(HistoryUnit.LAST_30, device);
            yearData = dataService.getHistoric(HistoryUnit.YEAR, device);
            allData = dataService.getHistoric(HistoryUnit.ALL, device);

            StationStatistics stationStatistics = new StationStatistics();
            SummaryStatistics summaryStatistics = new SummaryStatistics();

            SummaryStatistics weekStatistics = populateStatistics(summaryStatistics, weekData);
            stationStatistics.setWeek(weekStatistics);

            SummaryStatistics monthStatistics = populateStatistics(summaryStatistics, monthData);
            stationStatistics.setMonth(monthStatistics);

            SummaryStatistics yearStatistics = populateStatistics(summaryStatistics, yearData);
            stationStatistics.setYear(yearStatistics);

            SummaryStatistics allStatistics = populateStatistics(summaryStatistics, allData);
            stationStatistics.setAll(allStatistics);

            stationStatistics.setDeviceID(device.getId());
            stationStatistics.setDate(new Date());

            save(stationStatistics);

            return stationStatistics;
        }

        private SummaryStatistics populateStatistics(SummaryStatistics summaryStatistics, List<Data> someData)
        {
            summaryStatistics.clear();
            for (Data someDatum : someData)
            {
                summaryStatistics.addValue(someDatum.getTemperature());
            }
            return summaryStatistics;
        }
    }
}
