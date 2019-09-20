package space.hideaway.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import space.hideaway.repositories.site.SearchCriteria;
import space.hideaway.repositories.site.SitePredicate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SitePredicateTest
{

    SearchCriteria mockSearchCriteria;
    SitePredicate sitePredicate;

    @Before
        public void setUp(){

        mockSearchCriteria = mock(SearchCriteria.class);

        sitePredicate = new SitePredicate(mockSearchCriteria);


    }

    @Test

        public void TestGetPredicateSearchIsNumberEqual(){

        Object newNumberObject = 56.7;

        String expectedResultToString = "siteMetadata.89.7 = 56.7";
        when(mockSearchCriteria.getValue()).thenReturn(newNumberObject);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("equal");

        String  actualResultToString = sitePredicate.getPredicate().toString();

        Assert.assertEquals(expectedResultToString,actualResultToString);
    }

    @Test

    public void TestGetPredicateSearchIsNumberGreater(){

        Object newNumberObject = 56.7;

        String expectedResultToString = "siteMetadata.89.7 >= 56.7";
        when(mockSearchCriteria.getValue()).thenReturn(newNumberObject);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("greater");

        String  actualResultToString = sitePredicate.getPredicate().toString();

        Assert.assertEquals(expectedResultToString,actualResultToString);
    }
    @Test

    public void TestGetPredicateSearchIsNumberLess(){

        Object newNumberObject = 56.7;

        String expectedResultToString = "siteMetadata.89.7 <= 56.7";
        when(mockSearchCriteria.getValue()).thenReturn(newNumberObject);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("less");

        String  actualResultToString = sitePredicate.getPredicate().toString();

        Assert.assertEquals(expectedResultToString,actualResultToString);
    }
    @Test

    public void TestGetPredicateSearchIsNumberNotEqual(){

        Object newNumberObject = 56.7;

        String expectedResultToString = "siteMetadata.89.7 != 56.7";
        when(mockSearchCriteria.getValue()).thenReturn(newNumberObject);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("not_equal");

        String  actualResultToString = sitePredicate.getPredicate().toString();

        Assert.assertEquals(expectedResultToString,actualResultToString);
    }

    @Test
    public void TestGetPredicateSearchIsJSONArray(){

        JSONArray newNumberObject = new JSONArray();
        newNumberObject.put(54.7);
        newNumberObject.put(435.4);

        String expectedResultToString = "siteMetadata.89.7 between 54.7 and 435.4";
        when(mockSearchCriteria.getValue()).thenReturn(newNumberObject);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("between");

        String  actualResultToString = sitePredicate.getPredicate().toString();

        Assert.assertEquals(expectedResultToString,actualResultToString);
    }

    @Test
    public void TestGetPredicateSearchIsBooleanAndEqual(){

        Object newBool = true;

        String expectedResultToString = "siteMetadata.89.7 = true";
        when(mockSearchCriteria.getValue()).thenReturn(newBool);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("equal");

        String  actualResultToString = sitePredicate.getPredicate().toString();

        Assert.assertEquals(expectedResultToString,actualResultToString);
    }

    @Test
    public void TestGetPredicateSearchIsBooleanAndNotEqual(){

        Object newBool = false;

        String expectedResultToString = "siteMetadata.89.7 != false";
        when(mockSearchCriteria.getValue()).thenReturn(newBool);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("not_equal");

        String  actualResultToString = sitePredicate.getPredicate().toString();


        Assert.assertEquals(expectedResultToString,actualResultToString);
    }

    @Test
    public void TestGetPredicateSearchIsElseAndEqual(){

        Object newBool = "Sky";

        String expectedResultToString = "containsIc(siteMetadata.89.7,Sky)";
        when(mockSearchCriteria.getValue()).thenReturn(newBool);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("equal");

        String  actualResultToString = sitePredicate.getPredicate().toString();


        Assert.assertEquals(expectedResultToString,actualResultToString);
    }

    @Test
    public void TestGetPredicateSearchIsElseAndNotEqual(){

        Object newBool = "Sky";

        String expectedResultToString = "!containsIc(siteMetadata.89.7,Sky)";
        when(mockSearchCriteria.getValue()).thenReturn(newBool);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("not_equal");

        String  actualResultToString = sitePredicate.getPredicate().toString();


        Assert.assertEquals(expectedResultToString,actualResultToString);
    }

    @Test
    public void TestGetPredicateSearchNullCase(){

        Object newBool = "Sky";

        when(mockSearchCriteria.getValue()).thenReturn(newBool);
        when(mockSearchCriteria.getKey()).thenReturn("89.7");
        when(mockSearchCriteria.getOperation()).thenReturn("");



        Assert.assertNull(sitePredicate.getPredicate());
    }
}
