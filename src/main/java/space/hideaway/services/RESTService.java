package space.hideaway.services;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.model.site.Site;
import space.hideaway.model.upload.UploadHistory;
import space.hideaway.services.data.DataServiceImplementation;
import space.hideaway.services.site.SiteServiceImplementation;
import space.hideaway.services.upload.UploadHistoryService;
import space.hideaway.services.user.UserServiceImplementation;
import space.hideaway.util.HistoryUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RESTService
{

    private final DataServiceImplementation dataServiceImplementation;
    private final SiteServiceImplementation siteServiceImplementation;
    private final UserServiceImplementation userServiceImplementation;
    private final UploadHistoryService uploadHistoryService;
    private final DeviceService deviceService;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Autowired
    public RESTService(
            SiteServiceImplementation siteServiceImplementation,
            UserServiceImplementation userServiceImplementation,
            DataServiceImplementation dataServiceImplementation,
            UploadHistoryService uploadHistoryService,
            DeviceService deviceService)
    {
        this.siteServiceImplementation = siteServiceImplementation;
        this.dataServiceImplementation = dataServiceImplementation;
        this.userServiceImplementation = userServiceImplementation;
        this.uploadHistoryService = uploadHistoryService;
        this.deviceService = deviceService;
    }

    /**
     * Obtain site, upload, and record count for the currently logged in user.
     *
     * @return See populateInfocard method in space.hideaway.controllers.RESTController for more info.
     */
    public InfoCardSerializer populateInfocards()
    {
        User currentLoggedInUser = userServiceImplementation.getCurrentLoggedInUser();
        InfoCardSerializer infoCardSerializer = new InfoCardSerializer();
        infoCardSerializer.setSiteCount(siteServiceImplementation.countByUserID(currentLoggedInUser));
        infoCardSerializer.setRecordCount(dataServiceImplementation.countByUserID(currentLoggedInUser));
        infoCardSerializer.setUploadCount(uploadHistoryService.countByUserID(currentLoggedInUser));
        infoCardSerializer.setDeviceCount(deviceService.countByUserID(currentLoggedInUser));
        return infoCardSerializer;
    }

    /**
     * Obtain a list of sites for the currently logged in user.
     *
     * @return See populateSites method in space.hideaway.controllers.RESTController for more info.
     */
    public List<Site> populateSites()
    {
        User currentLoggedInUser = userServiceImplementation.getCurrentLoggedInUser();
        ArrayList<Site> siteList = new ArrayList<>(currentLoggedInUser.getSiteSet());
        return siteList.stream().sorted(Comparator.comparing(Site::getSiteName)).collect(Collectors.toList());
    }

    /**
     * Obtain a list of devices for the currently logged in user.
     *
     * @return See populateDevices method in space.hideaway.controllers.RESTController for more info.
     */
    public List<Device> populateDevices(){
        User currentLoggedInUser = userServiceImplementation.getCurrentLoggedInUser();
        ArrayList<Device> deviceList = new ArrayList<>(currentLoggedInUser.getDeviceSet());
        return deviceList.stream().sorted(Comparator.comparing(Device::getDeviceType)).collect(Collectors.toList());
    }

    /**
     * Obtain a list of upload history records for the currently logged in user.
     *
     * @param historyUnit How far back in time (week, month, year, all)
     * @return See getUploadHistory method in space.hideaway.controllers.RESTController for more info.
     */
    public List<UploadHistory> getUploadHistory(HistoryUnit historyUnit)
    {
        User currentLoggedInUser = userServiceImplementation.getCurrentLoggedInUser();
        switch (historyUnit)
        {
            case WEEK:
                return uploadHistoryService.getLastWeek(currentLoggedInUser);
            case LAST_30:
                return uploadHistoryService.getLastMonth(currentLoggedInUser);
            case YEAR:
                return uploadHistoryService.getLastYear(currentLoggedInUser);
        }
        return uploadHistoryService.getLastMonth(currentLoggedInUser);
    }

    /**
     * Obtain a list of sites for the currently logged in user.
     *
     * @return See populateSites method in space.hideaway.controllers.RESTController for more info.
     */
    public List<Site> populateSitesByQuery(String query, String location, String range)
    {
        double latitude = 0;
        double longitude = 0;
        double desiredRange = 0;

        List<Site> siteList = new ArrayList<>();

        // Checks if the search contains a location
        boolean locationPresent = location != null && !location.isEmpty();

        try
        {
            if (locationPresent)
            {
                // Parses the location and range strings into doubles
                String[] locationComponents = location.replaceAll(" ", "").split(",");
                latitude = Double.parseDouble(locationComponents[0]);
                longitude = Double.parseDouble(locationComponents[1]);
                desiredRange = Double.parseDouble(range.replace("km", "").trim());
            }
        } catch (Exception e)
        {
            return siteList.stream().sorted(Comparator.comparing(Site::getSiteName)).collect(Collectors.toList());
        }

        // Creates the Query
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Site.class).get();

        // Query into a boolean query. Meaning you can query by location OR keywords OR empty.
        BooleanJunction<BooleanJunction> booleanQuery = queryBuilder.bool();

        // Query by keywords
        if (query != null && !query.isEmpty())
        {
            booleanQuery.must(queryBuilder.keyword().fuzzy().onFields("siteName", "siteDescription", "user" +
                    ".username").matching(query).createQuery());
        }

        // Query by location
        if (locationPresent)
        {
            booleanQuery.must(queryBuilder.spatial().onField("location").within(desiredRange, Unit.KM).ofLatitude
                    (latitude).andLongitude(longitude).createQuery());
        }

        // Query by empty. Returns all sites if search fields are empty.
        if(!locationPresent && (query == null || query.isEmpty())){
            booleanQuery.must(queryBuilder.all().createQuery());
        }

        // Compiling queries into one query.
        Query builtQuery = booleanQuery.createQuery();

        // Executing full query.
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(builtQuery, Site.class);

        // Sorts sites by distance.
        if (locationPresent)
        {
            Sort distanceSort = queryBuilder.sort().byDistance().onField("location").fromLatitude(latitude)
                    .andLongitude(longitude).createSort();
            fullTextQuery.setSort(distanceSort);
        }

        // Ordered query results.
        List resultList = fullTextQuery.getResultList();

        // Filters the results to Site objects.
        for (Object o : resultList)
        {
            if (o instanceof Site)
            {
                siteList.add(((Site) o));
            }
        }
        return siteList.stream().sorted(Comparator.comparing(Site::getSiteName)).collect(Collectors.toList());
    }
}
