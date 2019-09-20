package space.hideaway.util;
import org.junit.*;
import space.hideaway.repositories.site.SearchCriteria;


public class SearchCriteriaTest {

        String expectedKey ;
        String expectedOperation;
        Object expectedValue;
        SearchCriteria searchCriteria;
        @Before
            public void setUp(){
            expectedKey = "key";
            expectedOperation = "operation";
            expectedValue = new Object();
            searchCriteria = new SearchCriteria(expectedKey, expectedOperation, expectedValue);

        }


    @Test
    public void TestGetAndSetKey(){

        String expectedKeyLocal = "NewKey";
        searchCriteria.setKey(expectedKeyLocal);

        Assert.assertEquals(expectedKeyLocal, searchCriteria.getKey());

    }

    @Test
    public void TestGetAndSetOperation(){

        String expectedOperationLocal = "newOp";
        searchCriteria.setOperation(expectedOperationLocal);

        Assert.assertEquals(expectedOperationLocal, searchCriteria.getOperation());

    }

    @Test
    public void TestGetAndSetValue(){

        Object expectedNewObject = new Object();

        searchCriteria.setValue(expectedNewObject);

        Assert.assertEquals(expectedNewObject, searchCriteria.getValue());

    }


        @Test
            public void TestInit(){

            Assert.assertEquals(expectedKey, searchCriteria.getKey());
            Assert.assertEquals(expectedOperation, searchCriteria.getOperation());
            Assert.assertEquals(expectedValue, searchCriteria.getValue());


        }

}
