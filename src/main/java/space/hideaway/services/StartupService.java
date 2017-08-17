package space.hideaway.services;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.logging.Logger;


@Component
public class StartupService
{

    private Logger logger = Logger.getLogger(getClass().getName());

    private final EntityManager entityManager;

    @Autowired
    public StartupService(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public void initialize()
    {
        logger.info("Linking entityManager.");
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        logger.info(String.format("entityManager: %s linked.", entityManager));

        try
        {
            logger.info("Beginning to index data.");
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Index complete.");
    }

}
