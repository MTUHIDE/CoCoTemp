package space.hideaway.services;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.hideaway.model.Role;
import space.hideaway.repositories.RoleRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.logging.Logger;


@Component
public class StartupService
{

    private Logger logger = Logger.getLogger(getClass().getName());


    private final EntityManagerFactory entityManagerFactory;

    private final RoleRepository roleRepository;

    @Autowired
    public StartupService(EntityManagerFactory entityManagerFactory, RoleRepository roleRepository)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.roleRepository = roleRepository;
    }

    /**
     * Call at the startup of the server.
     *
     *  - Enables Hibernate's FullTextEntityManager for Full-text searching.
     *  - Initializes roles in database
     */
    @Transactional
     public void initialize()
    {
        EntityManager entityManager= entityManagerFactory.createEntityManager();
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

        // Initialize user roles in database
        Role publicRole = createRole(1, "ROLE_PUBLIC");
        roleRepository.save(publicRole);
        Role adminRole = createRole(2, "ROLE_ADMIN");
        roleRepository.save(adminRole);
    }

    /**
     * Creates a user role which can restrict access on the site
     *
     * @param id - Uniq role id
     * @param name - Name of role (ex. 'ROLE_PUBLIC' & 'ROLE_ADMIN')
     * @return A role
     */
    private Role createRole(long id, String name)
    {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }

}
