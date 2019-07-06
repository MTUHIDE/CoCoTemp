package space.hideaway.util;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.*;
import org.hibernate.search.query.dsl.sort.*;
import org.hibernate.search.spatial.impl.SpatialNumericDocValueField;
import org.junit.*;
import org.mockito.Mock;
import org.springframework.ui.Model;
import space.hideaway.controllers.SearchController;
import space.hideaway.model.site.Site;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.*;

public class SearchControllerTest {



    SearchController searchController;
    SearchController spySearchController;

    @Before
        public void setUp(){
        searchController = new SearchController();
        spySearchController = spy(searchController);
    }

//
//    @Test
//        public void TestRenderSearchWithKeywordAndSpatial(){
//        String expectedQuery= "query";
//        String expectedLocation = "14.5,13.5";
//        String expectedRange = "27km";
//        Model MockModel = mock(Model.class);
////
////        QueryBuilder MockQueryBuilder = mock(QueryBuilder.class);
////
////        BooleanJunction<BooleanJunction> MockBooleanJunction = mock(BooleanJunction.class);
////
////        Logger MockLogger = mock(Logger.class);
////        FullTextEntityManager MockFullTextEntityManager = mock(FullTextEntityManager.class);
////
////        FullTextQuery MockFullTextQuery = mock(FullTextQuery.class);
////        Query MockQuery = mock(Query.class);
////        List MockList = mock(List.class);
//        Site MockSite = mock(Site.class);
//        List<Site> siteList = new ArrayList<Site>();
//        siteList.add(MockSite);
////
////        when(spySearchController.checkLocations(expectedLocation,expectedRange)).thenReturn(true);
////        when(spySearchController.queryBuilder(any(Logger.class),any(FullTextEntityManager.class))).thenReturn(MockQueryBuilder);
////        when(spySearchController.QueryByKeyword(matches(expectedQuery),any(Logger.class),any(QueryBuilder.class))).thenReturn(MockBooleanJunction);
////        doNothing().when(spySearchController).QueryByLocation(anyBoolean(),any(Logger.class),any(BooleanJunction.class),any(QueryBuilder.class));
////        doNothing().when(spySearchController).QueryByEmpty(anyBoolean(),matches(expectedQuery),any(BooleanJunction.class), any(QueryBuilder.class));
////        when(spySearchController.QueryCompiler(any(Logger.class),any(BooleanJunction.class))).thenReturn(MockQuery);
////        when(spySearchController.ExecuteQuery(any(Logger.class),any(FullTextEntityManager.class),any(Query.class))).thenReturn(MockFullTextQuery);
////        when(spySearchController.SortSites(anyBoolean(),any(QueryBuilder.class),any(FullTextQuery.class))).thenReturn(MockList);
////        when(spySearchController.FilterSites(MockList)).thenReturn(siteList);
////
//        Assert.assertEquals("search/search-page",spySearchController.renderSearchWithKeywordAndSpatial(expectedQuery,expectedLocation,expectedRange,MockModel));
//
//        verify(MockModel).addAttribute("nosites",false);
//        verify(MockModel).addAttribute("siteList",siteList);
//
//
//
//
//    }


    @Test
        public void TestQueryBuilder(){

        Logger MockLogger = mock(Logger.class);
        FullTextEntityManager MockFullTextEntityManager = mock(FullTextEntityManager.class);
        QueryBuilder MockQueryBuilder = mock(QueryBuilder.class);

        doNothing().when(MockLogger).info("Started location search.");
        SearchFactory MockSearchFactory = mock(SearchFactory.class);
        QueryContextBuilder MockQueryContextBuilder = mock(QueryContextBuilder.class);
        EntityContext MockEntityContext = mock(EntityContext.class);
        when(MockFullTextEntityManager.getSearchFactory()).thenReturn(MockSearchFactory);
        when(MockSearchFactory.buildQueryBuilder()).thenReturn(MockQueryContextBuilder);
        when(MockQueryContextBuilder.forEntity(Site.class)).thenReturn(MockEntityContext);
        when(MockEntityContext.get()).thenReturn(MockQueryBuilder);
        Assert.assertEquals(MockQueryBuilder,searchController.queryBuilder(MockLogger,MockFullTextEntityManager));

        verify(MockLogger).info("Started location search.");
    }

    @Test
        public void TestQueryByKeyword(){
        BooleanJunction<BooleanJunction> MockBooleanJunction =mock(BooleanJunction.class);
        String expectedQuery = "Query";
        Query MockQuery = mock(Query.class);
        Logger MockLogger = mock(Logger.class);
        TermTermination MockTermTermination = mock(TermTermination.class);
        FuzzyContext MockFuzzyContext = mock(FuzzyContext.class);
        TermContext MockTermContext = mock(TermContext.class);
        TermMatchingContext MockTermMatchingContext = mock(TermMatchingContext.class);
        MustJunction MockMustJunction = mock(MustJunction.class);
        QueryBuilder MockQueryBuilder = mock(QueryBuilder.class);
        when(MockQueryBuilder.bool()).thenReturn(MockBooleanJunction);
        when(MockQueryBuilder.keyword()).thenReturn(MockTermContext);
        when(MockTermContext.fuzzy()).thenReturn(MockFuzzyContext);
        when(MockFuzzyContext.onFields("siteName","siteDescription","user"+".username")).thenReturn(MockTermMatchingContext);
        when(MockTermMatchingContext.matching(expectedQuery)).thenReturn(MockTermTermination);
        when(MockTermTermination.createQuery()).thenReturn(MockQuery);
        when(MockBooleanJunction.must(MockQuery)).thenReturn(MockMustJunction);

        Assert.assertEquals(MockBooleanJunction, searchController.QueryByKeyword(expectedQuery,MockLogger,MockQueryBuilder));

        verify(MockLogger).info("Keyword detected: "+expectedQuery);
        verify(MockLogger).info("Keyword query built.");

    }

    @Test
        public void TestQueryByLocation(){
        boolean locationPresent =  true;
        BooleanJunction<BooleanJunction> MockBooleanJunction = mock(BooleanJunction.class);
        Logger MockLogger= mock(Logger.class);
        Query MockQuery = mock(Query.class);
        QueryBuilder MockQueryBuilder = mock(QueryBuilder.class);
        WithinContext MockWithingContext = mock(WithinContext.class);
        MustJunction MockMustJunction = mock(MustJunction.class);
        SpatialTermination MockSpatialTermination = mock(SpatialTermination.class);
        WithinContext.LongitudeContext MockLongitudeContext = mock(WithinContext.LongitudeContext.class);
        SpatialMatchingContext MockSpatialMatchingContext = mock(SpatialMatchingContext.class);
        SpatialContext MockSpatialContext = mock(SpatialContext.class);
        String loggedmsg = String.format("Latitude: %f Longitude: %f Range: %f", (double)0, (double)0, (double)0);
        when(MockQueryBuilder.spatial()).thenReturn(MockSpatialContext);
        when(MockSpatialContext.onField("location")).thenReturn(MockSpatialMatchingContext);
        when(MockSpatialMatchingContext.within(0,Unit.KM)).thenReturn(MockWithingContext);
        when(MockWithingContext.ofLatitude(0)).thenReturn(MockLongitudeContext);
        when(MockLongitudeContext.andLongitude(0)).thenReturn(MockSpatialTermination);
        when(MockSpatialTermination.createQuery()).thenReturn(MockQuery);
        when(MockBooleanJunction.must(MockQuery)).thenReturn(MockMustJunction);

        searchController.QueryByLocation(locationPresent,MockLogger,MockBooleanJunction,MockQueryBuilder);


        verify(MockLogger).info("Location detected.");
        verify(MockLogger).info(loggedmsg);
        verify(MockLogger).info("Location query built.");

    }

    @Test
        public void  TestQueryByEmpty(){
        Boolean locationPresent = false;
        String expectedQuery = null;
        BooleanJunction<BooleanJunction> MockBooleanJunction = mock(BooleanJunction.class);
        QueryBuilder MockQueryBuilder = mock(QueryBuilder.class);
        AllContext MockAllContext= mock(AllContext.class);
        Query MockQuery = mock(Query.class);
        MustJunction MockMustJunction = mock(MustJunction.class);
        when(MockQueryBuilder.all()).thenReturn(MockAllContext);
        when(MockAllContext.createQuery()).thenReturn(MockQuery);
        when(MockBooleanJunction.must(MockQuery)).thenReturn(MockMustJunction);

        searchController.QueryByEmpty(locationPresent,expectedQuery,MockBooleanJunction,MockQueryBuilder);

        verify(MockBooleanJunction).must(MockQuery);
    }

    @Test
        public void TestQueryCompiler(){
        Logger MockLogger = mock(Logger.class);
        BooleanJunction<BooleanJunction> MockBooleanJunction = mock(BooleanJunction.class);
        Query MockQuery = mock(Query.class);

        when(MockBooleanJunction.createQuery()).thenReturn(MockQuery);

        Assert.assertEquals(MockQuery,searchController.QueryCompiler(MockLogger,MockBooleanJunction));
        verify(MockLogger).info("Compiling queries into super query.");

    }

    @Test
        public void TestExecuteQuery(){
        Logger MockLogger = mock(Logger.class);
        FullTextEntityManager MockFullTextEntityManager = mock(FullTextEntityManager.class);
        Query MockQuery = mock(Query.class);
        FullTextQuery MockFullTextQuery = mock(FullTextQuery.class);

        when(MockFullTextEntityManager.createFullTextQuery(MockQuery,Site.class)).thenReturn(MockFullTextQuery);
        Assert.assertEquals(MockFullTextQuery,searchController.ExecuteQuery(MockLogger,MockFullTextEntityManager,MockQuery));

        verify(MockLogger).info("Executing full query.");

    }

    @Test
        public void TestSortSites(){
        Boolean locationPresent = true;
        QueryBuilder MockQueryBuilder = mock(QueryBuilder.class);
        FullTextQuery MockFullTextQuery = mock(FullTextQuery.class);
        Sort MockSort = mock(Sort.class);
        List MockList = mock(List.class);
        SortDistanceFieldAndReferenceContext MockSortDistAndRefcontext= mock(SortDistanceFieldAndReferenceContext.class);
        SortLatLongContext MockSortLatLongContext = mock(SortLatLongContext.class);
        SortContext MockSortContext = mock(SortContext.class);
        SortDistanceFieldContext MockSortDistFieldContext = mock(SortDistanceFieldContext.class);
        SortDistanceNoFieldContext MockSortDistNoFieldContext = mock(SortDistanceNoFieldContext.class);

        when(MockQueryBuilder.sort()).thenReturn(MockSortContext);
        when(MockSortContext.byDistance()).thenReturn(MockSortDistNoFieldContext);
        when(MockSortDistNoFieldContext.onField("location")).thenReturn(MockSortDistFieldContext);
        when(MockSortDistFieldContext.fromLatitude((double)0)).thenReturn(MockSortLatLongContext);
        when(MockSortLatLongContext.andLongitude(0.0)).thenReturn(MockSortDistAndRefcontext);
        when(MockSortDistAndRefcontext.createSort()).thenReturn(MockSort);
        when(MockFullTextQuery.setSort(MockSort)).thenReturn(MockFullTextQuery);
        when(MockFullTextQuery.getResultList()).thenReturn(MockList);

        Assert.assertEquals(MockList,searchController.SortSites(locationPresent,MockQueryBuilder,MockFullTextQuery));

    }

    @Test
        public void TestFilterSites(){
        List startSiteList = new ArrayList();
        Site MockSiteOne = mock(Site.class);
        Site MockSiteTwo = mock(Site.class);
        List<Site> siteList = new ArrayList<>();

        siteList.add(MockSiteOne);
        siteList.add(MockSiteTwo);

        startSiteList.add(MockSiteOne);
        startSiteList.add(MockSiteTwo);

        Assert.assertTrue(siteList.equals(searchController.FilterSites(startSiteList)));

    }
    @Test
        public void TestRenderSearchpage(){
        Model MockModel = mock(Model.class);

        Assert.assertEquals("search/search-page",searchController.renderSearchPage(MockModel));

        verify(MockModel).addAttribute("nosites",true);

    }

}
