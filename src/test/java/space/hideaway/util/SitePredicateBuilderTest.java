package space.hideaway.util;
import org.junit.*;
import space.hideaway.repositories.site.SitePredicatesBuilder;

public class SitePredicateBuilderTest {

    SitePredicatesBuilder sitePredicatesBuilder;

    @Before
        public void setUp(){

        sitePredicatesBuilder = new SitePredicatesBuilder();
    }

    @Test
        public void TestWithNotCalledBuild(){
        String dummyString = "fg";
        Assert.assertNull(sitePredicatesBuilder.build(dummyString));

    }

    @Test
    public void TestWithCalledBuildTypeOR(){
        String expectedType = "OR";
        String expectedKey = "key";
        String expectedKey2 = "OneKeY";

        String expectedOp = "less";
        Object expectedNumber = 45.6;
        Object expectedNumberTwo = 456465.76;

        String expetedResultToString = "siteMetadata.key <= 45.6 || siteMetadata.OneKeY <= 456465.76";
        sitePredicatesBuilder.with(expectedKey,expectedOp,expectedNumber);
        sitePredicatesBuilder.with(expectedKey2,expectedOp,expectedNumberTwo);

        Assert.assertEquals(expetedResultToString, sitePredicatesBuilder.build(expectedType).toString());

    }

    @Test
    public void TestWithCalledBuildOtherType(){
        String expectedType = "AND";
        String expectedKey = "key";
        String expectedKey2 = "OneKeY";
        String expectedOp = "less";
        Object expectedNumber = 45.6;
        Object expectedNumberTwo = 456465.76;

        String expetedResultToString = "siteMetadata.key <= 45.6 && siteMetadata.OneKeY <= 456465.76";
        sitePredicatesBuilder.with(expectedKey,expectedOp,expectedNumber);
        sitePredicatesBuilder.with(expectedKey2,expectedOp,expectedNumberTwo);

        Assert.assertEquals(expetedResultToString, sitePredicatesBuilder.build(expectedType).toString());

    }

}
