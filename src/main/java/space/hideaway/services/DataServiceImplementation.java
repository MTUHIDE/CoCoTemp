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

/**
 * Created by dough on 11/7/2016.
 */


@Service
public class DataServiceImplementation implements DataService {

    Logger logger = Logger.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataRepository dataRepository;

    @Value("${hibernate.jdbc.batch_size}")
    private int batchSize;

    @Override
    public void save(Data data) {
        dataRepository.save(data);
    }

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
        }
        transaction.commit();
        session.close();
        return dataList;
    }


}
