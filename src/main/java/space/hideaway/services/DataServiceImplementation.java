package space.hideaway.services;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.repositories.DataRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class DataServiceImplementation implements DataService {

    private final DataRepository dataRepository;
    Logger logger = Logger.getLogger(getClass());
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    public DataServiceImplementation(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Save a new data point into the database.
     *
     * @param data The new data point to be saved.
     */
    @Override
    public void save(Data data) {
        dataRepository.save(data);
    }

    /**
     * Batch save a large amount of data points.
     *
     * @param dataList A list of points to be saved.
     * @return The newly saved list.
     */
    @Override
    @Transactional
    public List<Data> batchSave(List<Data> dataList) {
        Session session = entityManager.getEntityManagerFactory().createEntityManager().unwrap(Session.class);
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.get(i);
            session.save(data);
            if (i % batchSize == 0) {
                session.flush();
                session.clear();
            }
            System.out.println(i);
        }
        transaction.commit();
        session.close();
        return dataList;
    }

    /**
     * Get a list of average temperature points for each device on the current day.
     *
     * @return A list of points, each representing the average recorded point for the current day.
     */
    @Override
    public List<Data> getAverageDataForCurrentDay() {
        return dataRepository.getAverageDataForCurrentDay();
    }

    /**
     * Get the most current record for a given device.
     *
     * @param device The device to obtain the most recent point for.
     * @return The most current point for a given device. If no point is found, a dummy record is returned
     * with extreme values.
     */
    @Override
    public Data getMostCurrentRecord(Device device) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> query = criteriaBuilder.createQuery();
        Root<Data> from = query.from(Data.class);
        ParameterExpression<UUID> parameter = criteriaBuilder.parameter(UUID.class);
        query.select(from)
                .where(criteriaBuilder.equal(from.get("deviceID"), parameter))
                .orderBy(criteriaBuilder.desc(from.get("dateTime")));
        TypedQuery<Object> finalQuery = entityManager.createQuery(query);

        try {
            return (Data) finalQuery.setParameter(parameter, device.getId()).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return new Data(null, null, null, -99999);
        }

    }

    /**
     * Get a list of data points for a user.
     *
     * @param user The user to obtain a list of data points for.
     * @return A list of data points for the given user.
     */
    @Override
    public Set<Data> getAllData(User user) {
        return dataRepository.findByUserID(user.getId());
    }
}
