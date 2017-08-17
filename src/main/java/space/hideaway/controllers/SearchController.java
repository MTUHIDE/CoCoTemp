package space.hideaway.controllers;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import space.hideaway.model.Site;
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
    private Logger logger = Logger.getLogger(getClass().getName());

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @RequestMapping(value = "/search", params = {"type=site"})
    @Transactional
    public String renderSearchWithKeywordAndSpatial(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "range", required = false) String range,
            Model model)
    {

        double latitude = 0;
        double longitude = 0;
        double desiredRange = 0;

        boolean locationPresent = location != null && !location.isEmpty();

        try
        {
            if (locationPresent)
            {
                String[] locationComponents = location.replaceAll(" ", "").split(",");
                latitude = Double.parseDouble(locationComponents[0]);
                longitude = Double.parseDouble(locationComponents[1]);
                desiredRange = Double.parseDouble(range.replace("km", "").trim());
            }
        } catch (Exception e)
        {
            return "redirect:/search?error";
        }

        model.addAttribute("statisticsUtils", new StatisticsUtils());
        logger.info("Started location search.");
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Site.class).get();

        BooleanJunction<BooleanJunction> booleanQuery = queryBuilder.bool();
        if (query != null && !query.isEmpty())
        {
            logger.info("Keyword detected: " + query);
            booleanQuery.must(queryBuilder.keyword().fuzzy().onFields("siteName", "siteDescription", "user" +
                    ".username").matching(query).createQuery());
            logger.info("Keyword query built.");
        }

        if (locationPresent)
        {
            logger.info("Location detected.");

            logger.info(String.format("Latitude: %f Longitude: %f Range: %f", latitude, longitude, desiredRange));
            booleanQuery.must(queryBuilder.spatial().onField("location").within(desiredRange, Unit.KM).ofLatitude
                    (latitude).andLongitude(longitude).createQuery());
            logger.info("Location query built.");
        }

        // Returns all sites if search fields are empty
        if(!locationPresent && (query == null || query.isEmpty())){
            booleanQuery.must(queryBuilder.all().createQuery());
        }

        logger.info("Compiling queries into super query.");
        Query builtQuery = booleanQuery.createQuery();

        logger.info("Executing full query.");
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(builtQuery, Site.class);

        if (locationPresent)
        {
            Sort distanceSort = queryBuilder.sort().byDistance().onField("location").fromLatitude(latitude)
                                            .andLongitude(longitude).createSort();
            fullTextQuery.setSort(distanceSort);
        }

        List resultList = fullTextQuery.getResultList();

        List<Site> siteList = new ArrayList<>();
        for (Object o : resultList)
        {
            if (o instanceof Site)
            {
                siteList.add(((Site) o));
            }
        }

        model.addAttribute("nosites", siteList.isEmpty());
        model.addAttribute("siteList", siteList);

        return "search/search-page";
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String renderSearchPage(Model model)
    {
        model.addAttribute("nosites", true);
        model.addAttribute("siteList", new ArrayList<Site>());
        model.addAttribute("statisticsUtils", new StatisticsUtils());
        return "search/search-page";
    }
}
