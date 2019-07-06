package space.hideaway.controllers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.lucene.search.BooleanQuery;
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
import space.hideaway.model.site.Site;
import space.hideaway.util.StatisticsUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dough
 * 2017-03-01
 *
 * Edited by Justin Havely
 * 8/18/17
 *
 * Serves the search page to the user.
 */
@Controller
@Transactional
public class SearchController
{

    double latitude = 0;
    double longitude = 0;
    double desiredRange = 0;

    //Logs the search process, also used for debugging.

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    /**
     * The mapping for the search page.
     *
     * @param query Keyword(s) entered by the user.
     * @param location Location entered by the user.
     * @param range Range entered by the user.
     * @param model The Spring model for the search page.
     * @return The template for the search page.
     */
    @RequestMapping(value = "/search", params = {"type=site"})
    @Transactional
    public String renderSearchWithKeywordAndSpatial(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "range", required = false) String range,
            Model model)
    {
        boolean locationPresent;
        Logger logger = Logger.getLogger(getClass().getName());
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        try
        {
            locationPresent = checkLocations(location,range);
    } catch (Exception e)
    {
        return "redirect:/search?error";
    }
        // Adds site statistics to the model
        model.addAttribute("statisticsUtils", new StatisticsUtils());

        QueryBuilder queryBuilder = queryBuilder(logger,fullTextEntityManager);

        BooleanJunction<BooleanJunction> booleanQuery = QueryByKeyword(query,logger, queryBuilder);

        QueryByLocation(locationPresent,logger,booleanQuery, queryBuilder);

        QueryByEmpty(locationPresent,query,booleanQuery, queryBuilder);

        Query builtQuery = QueryCompiler(logger,booleanQuery);

        FullTextQuery fullTextQuery = ExecuteQuery(logger,fullTextEntityManager,builtQuery);

        List resultList= SortSites(locationPresent,queryBuilder,fullTextQuery);

        List<Site> siteList = FilterSites(resultList);

        // Finally, adds sites to model
        model.addAttribute("nosites", siteList.isEmpty());
        model.addAttribute("siteList", siteList);

        return "search/search-page";
    }

    public boolean checkLocations(String location, String range){

        // Checks if the search contains a location
        boolean locationPresent = location != null && !location.isEmpty();


            if (locationPresent)
            {
                // Parses the location and range strings into doubles
                String[] locationComponents = location.replaceAll(" ", "").split(",");
                latitude = Double.parseDouble(locationComponents[0]);
                longitude = Double.parseDouble(locationComponents[1]);
                desiredRange = Double.parseDouble(range.replace("km", "").trim());
            }
            return locationPresent;
    }


    public QueryBuilder queryBuilder(Logger logger, FullTextEntityManager fullTextEntityManager){

        // Creates the Query
        logger.info("Started location search.");
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Site.class).get();
        return queryBuilder;
    }

    public BooleanJunction<BooleanJunction> QueryByKeyword(String query,Logger logger, QueryBuilder queryBuilder){
        BooleanJunction<BooleanJunction> booleanQuery = queryBuilder.bool();
        // Query into a boolean query. Meaning you can query by location OR keywords OR empty.

        // Query by keywords

        if (query != null && !query.isEmpty())
        {
            logger.info("Keyword detected: " + query);
            booleanQuery.must(queryBuilder.keyword().fuzzy().onFields("siteName", "siteDescription", "user" +
                    ".username").matching(query).createQuery());
            logger.info("Keyword query built.");
        }
        return booleanQuery;

    }

    public void QueryByLocation(boolean locationPresent, Logger logger, BooleanJunction<BooleanJunction> booleanQuery, QueryBuilder queryBuilder){

        // Query by location
        if (locationPresent)
        {
            logger.info("Location detected.");

            logger.info(String.format("Latitude: %f Longitude: %f Range: %f", latitude, longitude, desiredRange));
            booleanQuery.must(queryBuilder.spatial().onField("location").within(desiredRange, Unit.KM).ofLatitude
                    (latitude).andLongitude(longitude).createQuery());
            logger.info("Location query built.");
        }
    }

    public void QueryByEmpty(Boolean locationPresent, String query, BooleanJunction<BooleanJunction> booleanQuery, QueryBuilder queryBuilder) {
        // Query by empty. Returns all sites if search fields are empty.
        if (!locationPresent && (query == null || query.isEmpty())) {
            booleanQuery.must(queryBuilder.all().createQuery());
        }
    }

    public Query QueryCompiler(Logger logger, BooleanJunction<BooleanJunction> booleanQuery){

        // Compiling queries into one query.
        logger.info("Compiling queries into super query.");
        Query builtQuery = booleanQuery.createQuery();

        return builtQuery;
    }

    public FullTextQuery ExecuteQuery(Logger logger, FullTextEntityManager fullTextEntityManager, Query builtQuery){

        // Executing full query.
        logger.info("Executing full query.");
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(builtQuery, Site.class);

        return fullTextQuery;
    }

    public List SortSites(Boolean locationPresent, QueryBuilder queryBuilder, FullTextQuery fullTextQuery){

        // Sorts sites by distance.
        if (locationPresent)
        {
            Sort distanceSort = queryBuilder.sort().byDistance().onField("location").fromLatitude(latitude)
                    .andLongitude(longitude).createSort();
            fullTextQuery.setSort(distanceSort);
        }

        // Ordered query results.
        List resultList = fullTextQuery.getResultList();

        return resultList;
    }

    public List<Site> FilterSites(List resultList){

        // Filters the results to Site objects.
        List<Site> siteList = new ArrayList<>();
        for (Object o : resultList)
        {
            if (o instanceof Site)
            {
                siteList.add(((Site) o));
            }
        }

        return siteList;

    }
    /**
     * The mapping for the search page.
     *
     * @param model The Spring model for the search page.
     * @return The template for the search page.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String renderSearchPage(Model model)
    {
        model.addAttribute("nosites", true);
        model.addAttribute("siteList", new ArrayList<Site>());
        model.addAttribute("statisticsUtils", new StatisticsUtils());
        return "search/search-page";
    }
}
