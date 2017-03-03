package space.hideaway.controllers;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import space.hideaway.model.Device;
import space.hideaway.model.SearchModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dough on 2017-03-01.
 */
@Controller
@Transactional
public class SearchController
{
    Logger logger = Logger.getLogger(getClass().getName());

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private
    EntityManager entityManager;


    /**
     * Form endpoint for getting a simple list of devices from a string.
     * Searches using :deviceName and :deviceDescription.
     * <p>
     * Redirects to the more feature complete search page complete with results.
     *
     * @param searchModel The search model attrubute injected into the form on the index contoller.
     * @return The name of the search page template.
     */
    @RequestMapping(value = "/search", params = {"_basic"}, method = RequestMethod.POST)
    @Transactional
    public String serveSearch(@ModelAttribute("searchModel") SearchModel searchModel)
    {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        String searchQuery = searchModel.getSearchString();
        logger.info("Search query was: " + searchQuery);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                                                         .buildQueryBuilder()
                                                         .forEntity(Device.class)
                                                         .get();
        Query query = queryBuilder.keyword().onFields(
                "deviceName",
                "deviceDescription")
                                  .matching(searchQuery)
                                  .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(query, Device.class);

        List result = jpaQuery.getResultList();

        for (Object o : result)
        {
            if (o instanceof Device)
            {
                Device device = (Device) o;
                logger.info(device.getDeviceName());
            }
        }

        return "search/search-page";
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String renderSearchPage(Model model)
    {
        model.addAttribute("deviceList", new ArrayList<Device>());
        return "search/search-page";
    }
}
