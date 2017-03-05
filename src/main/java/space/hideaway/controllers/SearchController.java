package space.hideaway.controllers;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import space.hideaway.model.Device;
import space.hideaway.util.StatisticsUtils;

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
     * @return The name of the search page template.
     */
    @RequestMapping(value = "/search", params = {"query"}, method = RequestMethod.GET)
    @Transactional
    public String serveSearch(@RequestParam("query") String searchQuery, Model model)
    {
        model.addAttribute("statisticsUtils", new StatisticsUtils());

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        logger.info("Search query was: " + searchQuery);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                                                         .buildQueryBuilder()
                                                         .forEntity(Device.class)
                                                         .get();
        Query query = queryBuilder.keyword().fuzzy().onFields(
                "deviceName",
                "deviceDescription", "user.username")
                                  .matching(searchQuery)
                                  .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(query, Device.class);

        List result = jpaQuery.getResultList();
        List<Device> castedList = new ArrayList<>();

        for (Object o : result)
        {
            if (o instanceof Device)
            {
                castedList.add((Device) o);
            }
        }

        model.addAttribute("noDevices", castedList.isEmpty());
        model.addAttribute("deviceList", castedList);

        return "search/search-page";
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String renderSearchPage(Model model)
    {
        model.addAttribute("noDevices", true);
        model.addAttribute("deviceList", new ArrayList<Device>());
        model.addAttribute("statisticsUtils", new StatisticsUtils());
        return "search/search-page";
    }
}
