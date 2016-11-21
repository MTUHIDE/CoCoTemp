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
            System.out.println(i);
        }
        transaction.commit();
        session.close();
        return dataList;
    }

    @Override
    public List<Data> getAverageDataForCurrentDay() {
        return dataRepository.getAverageDataForCurrentDay();
    }

    @Override
    public Data getLastRecording(Device device) {
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

    @Override
    public Set<Data> getAllData(User user) {
        return dataRepository.findByUserID(user.getId());
    }
}
