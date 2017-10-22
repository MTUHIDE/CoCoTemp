package space.hideaway.services.site;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Data;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteStatistics;
import space.hideaway.repositories.site.SiteStatisticsRepository;
import space.hideaway.services.data.DataService;
import space.hideaway.util.HistoryUnit;
import space.hideaway.util.SortingUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by dough
 * 2017-02-19
 */
@Service
public class SiteStatisticsServiceImplementation implements SiteStatisticsService
{

    private final SiteStatisticsRepository siteStatisticsRepository;
    private final DataService dataService;

    @Autowired
    public SiteStatisticsServiceImplementation(
            SiteStatisticsRepository siteStatisticsRepository,
            DataService dataService)
    {
        this.dataService = dataService;
        this.siteStatisticsRepository = siteStatisticsRepository;
    }

    /**
     * Calculates the statistics for a site. This should be called after new
     * records have been added to the site. Statistics are calculated from a
     * pool of available threads, so not to slow down the server.
     *
     * @param site The site to have statistics recalculated
     * @return The recalculated statistics
     */
    @Override
    public Future<SiteStatistics> recalculateStatistics(Site site)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(new StatisticsCalculationRunnable(site));
    }

    /**
     * Saves site statistics into the database.
     *
     * @param siteStatistics The site statistics to save
     * @return The saved site statistics
     */
    @Override
    public SiteStatistics save(SiteStatistics siteStatistics)
    {
        return siteStatisticsRepository.save(siteStatistics);
    }

    /**
     * Gets the most recent statistics for a site. If the site has no statistics
     * an 'empty' siteStatistics object is return with all statistics set to zero.
     *
     * @param site The site that statistics are being requested
     * @return The statistics
     */
    @Override
    public SiteStatistics getMostRecent(Site site)
    {
        List<SiteStatistics> siteStatistics = SortingUtils.sortMostRecentFirst(site.getSiteStatisticsList());
        if (siteStatistics.isEmpty())
        {
            return SiteStatistics.EMPTY_STATISTIC;
        } else
        {
            return siteStatistics.get(0);
        }
    }

    /**
     * A runnable class to calculate the statistics of a site.
     */
    private class StatisticsCalculationRunnable implements Callable<SiteStatistics>
    {

        Site site;
        List<Data> weekData;
        List<Data> monthData;
        List<Data> yearData;
        List<Data> allData;

        StatisticsCalculationRunnable(Site site)
        {
            this.site = site;
        }

        /**
         * The starting point on this class. Injects data sets in to
         * a summary statistics object for calculations.
         *
         * @return The recalculated statistics
         * @throws Exception
         */
        @Override
        public SiteStatistics call() throws Exception
        {
            // Gets data for a period of time
            weekData = dataService.getHistoric(HistoryUnit.WEEK, site);
            monthData = dataService.getHistoric(HistoryUnit.LAST_30, site);
            yearData = dataService.getHistoric(HistoryUnit.YEAR, site);
            allData = dataService.getHistoric(HistoryUnit.ALL, site);

            SiteStatistics siteStatistics = new SiteStatistics();

            // A class containing common statistical information about a set of values. (min, max, avg...)
            SummaryStatistics summaryStatistics = new SummaryStatistics();

            SummaryStatistics weekStatistics = populateStatistics(summaryStatistics, weekData);
            siteStatistics.setWeek(weekStatistics);

            SummaryStatistics monthStatistics = populateStatistics(summaryStatistics, monthData);
            siteStatistics.setMonth(monthStatistics);

            SummaryStatistics yearStatistics = populateStatistics(summaryStatistics, yearData);
            siteStatistics.setYear(yearStatistics);

            SummaryStatistics allStatistics = populateStatistics(summaryStatistics, allData);
            siteStatistics.setAll(allStatistics);

            siteStatistics.setSiteID(site.getId());
            siteStatistics.setDate(new Date());

            save(siteStatistics);

            return siteStatistics;
        }

        /**
         * Handles the process of injecting data into the summary statistics class for calculation.
         *
         * @param summaryStatistics The summary statistics object
         * @param someData The data set to calculate statistics with
         * @return The summary statistics object
         */
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
