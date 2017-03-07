package space.hideaway.services;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.hideaway.model.Data;
import space.hideaway.model.Site;
import space.hideaway.model.User;
import space.hideaway.repositories.DataRepository;
import space.hideaway.util.HistoryUnit;
import space.hideaway.util.SortingUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class DataServiceImplementation implements DataService
{

    private final DataRepository dataRepository;
    @Autowired
    StationStatisticsService stationStatisticsService;
    Logger logger = Logger.getLogger(getClass());
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    public DataServiceImplementation(DataRepository dataRepository)
    {
        this.dataRepository = dataRepository;
    }

    /**
     * Save a new data point into the database.
     *
     * @param data The new data point to be saved.
     */
    @Override
    public void save(Site site, Data data)
    {
        dataRepository.save(data);

        //Station statistics hook to recalculate statistics on new data.
        stationStatisticsService.recalculateStatistics(site);
    }

    /**
     * Batch save a large amount of data points.
     *
     * @param dataList A list of points to be saved.
     * @return The newly saved list.
     */
    @Override
    @Transactional
    public List<Data> batchSave(Site site, List<Data> dataList)
    {
        Session session = entityManager.getEntityManagerFactory().createEntityManager().unwrap(Session.class);
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < dataList.size(); i++)
        {
            Data data = dataList.get(i);
            session.save(data);
            if (i % batchSize == 0)
            {
                session.flush();
                session.clear();
            }
            System.out.println(i);
        }
        transaction.commit();
        session.close();

        //Station statistics hook to recalculate statistics on new data.
        stationStatisticsService.recalculateStatistics(site);
        return dataList;
    }

    @Override
    public Long countByUserID(User currentLoggedInUser)
    {
        return dataRepository.countByUserID(Math.toIntExact(currentLoggedInUser.getId()));
    }

    @Override
    public List<Data> getHistoric(HistoryUnit week, Site site)
    {
        switch (week)
        {
            case WEEK:
                return getDataDaysBack(site, 7);
            case LAST_30:
                return getDataDaysBack(site, 30);
            case YEAR:
                return getDataDaysBack(site, 365);
            case ALL:
                return SortingUtils.sortMostRecentFirst(site.getDataSet());
        }
        return getDataDaysBack(site, 7);
    }

    private List<Data> getDataDaysBack(Site site, int delta)
    {
        List<Data> dataList = SortingUtils.sortMostRecentFirst(site.getDataSet());
        List<Data> resultList = new ArrayList<>();

        Date mostRecent = dataList.get(0).getDateTime();
        logger.info("Most recent date calculated: " + mostRecent.toString());

        Date oldestAllowed = new DateTime(mostRecent).minusDays(delta).toDate();
        logger.info("Oldest date allowed: " + oldestAllowed.toString());

        for (Data data : dataList)
        {
            if (data.getDateTime().before(oldestAllowed))
            {
                break;
            } else
            {
                resultList.add(data);
            }
        }

        logger.info(String.format("Out of %s records, %s remain with a delta of %s", dataList.size(), resultList.size
                (), delta));

        return resultList;
    }
}
