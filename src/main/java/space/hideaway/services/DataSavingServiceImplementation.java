package space.hideaway.services;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.hideaway.model.Data;
import space.hideaway.repositories.DataRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service
public class DataSavingServiceImplementation implements DataSavingService
{

    private final DataRepository dataRepository;
    Logger logger = Logger.getLogger(getClass());
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    public DataSavingServiceImplementation(DataRepository dataRepository)
    {
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
}
