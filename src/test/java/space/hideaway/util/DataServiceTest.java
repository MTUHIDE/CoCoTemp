package space.hideaway.util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.loader.plan.exec.internal.OneToManyLoadQueryDetails;
import org.joda.time.DateTime;
import org.junit.*;
import org.mockito.*;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.repositories.DataRepository;
import space.hideaway.services.data.DataServiceImplementation;
import space.hideaway.model.Data;
import space.hideaway.services.site.SiteStatisticsService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class DataServiceTest {

    @InjectMocks
    DataServiceImplementation dataServiceImplementation;

    @Mock
    SiteStatisticsService siteStatisticsService = mock(SiteStatisticsService.class);

    @Mock
    EntityManager entityManager = mock(EntityManager.class);

    @Mock
    Logger logger = mock(Logger.class);


    @Captor
    ArgumentCaptor<Data> dataCaptor;

    DataRepository mockDataRepository;
    @Before
    public void setUp(){

        mockDataRepository = Mockito.mock(DataRepository.class);
        dataServiceImplementation = new DataServiceImplementation(mockDataRepository);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave(){
        Data mockData = mock(Data.class);



        Site mockSite = mock(Site.class);
        when(mockDataRepository.save(mockData)).thenReturn(mockData);
        dataServiceImplementation.save(mockSite,mockData);
        verify(mockDataRepository).save(mockData);
        verify(siteStatisticsService).recalculateStatistics(mockSite);


    }

    @Test
    public void testBatchSave(){
        org.springframework.test.util.ReflectionTestUtils.setField(dataServiceImplementation, "batchSize",50);
        Data mockData = mock(Data.class);
        Data mockData2 = mock(Data.class);
        List<Data>  dataList = new ArrayList<Data>();
        Session mockSession = mock(Session.class);
        Transaction mockTransaction = mock(Transaction.class);
        EntityManagerFactory mockEntityManagerFactory = mock(EntityManagerFactory.class);
        dataList.add(mockData);
        dataList.add(mockData2);
        when(entityManager.getEntityManagerFactory()).thenReturn(mockEntityManagerFactory);
        when(mockEntityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.unwrap(Session.class)).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();
        Site mockSite = mock(Site.class);



        List<Data> result = dataServiceImplementation.batchSave(mockSite, dataList);

        verify(mockTransaction).commit();
        verify(mockSession).close();
        verify(mockSession,times(1)).flush();
        verify(mockSession,times(1)).clear();
        verify(siteStatisticsService).recalculateStatistics(mockSite);
        Mockito.verify(mockSession, times(2)).save(dataCaptor.capture());
        List<Data> args = dataCaptor.getAllValues();
        Assert.assertEquals(Arrays.asList(mockData, mockData2),args);
        Assert.assertEquals(dataList,result);
    }

    @Test
    public void testBatchSizeWith50Values(){

        org.springframework.test.util.ReflectionTestUtils.setField(dataServiceImplementation, "batchSize",50);
        Data mockData = mock(Data.class);
        Data mockData2 = mock(Data.class);
        Data mockData3 = mock(Data.class);
        Data mockData4 = mock(Data.class);
        Data mockData5 = mock(Data.class);
        Data mockData6 = mock(Data.class);
        Data mockData7 = mock(Data.class);
        Data mockData8 = mock(Data.class);
        Data mockData9 = mock(Data.class);
        Data mockData10 = mock(Data.class);
        Data mockData11 = mock(Data.class);
        Data mockData12 = mock(Data.class);
        Data mockData13 = mock(Data.class);
        Data mockData14 = mock(Data.class);
        Data mockData15 = mock(Data.class);
        Data mockData16 = mock(Data.class);
        Data mockData17 = mock(Data.class);
        Data mockData18 = mock(Data.class);
        Data mockData19 = mock(Data.class);
        Data mockData20 = mock(Data.class);
        Data mockData21 = mock(Data.class);
        Data mockData22 = mock(Data.class);
        Data mockData23 = mock(Data.class);
        Data mockData24 = mock(Data.class);
        Data mockData25 = mock(Data.class);
        Data mockData26 = mock(Data.class);
        Data mockData27 = mock(Data.class);
        Data mockData28 = mock(Data.class);
        Data mockData29= mock(Data.class);
        Data mockData30 = mock(Data.class);
        Data mockData31 = mock(Data.class);
        Data mockData32 = mock(Data.class);
        Data mockData33 = mock(Data.class);
        Data mockData34 = mock(Data.class);
        Data mockData35 = mock(Data.class);
        Data mockData36 = mock(Data.class);
        Data mockData37 = mock(Data.class);
        Data mockData38 = mock(Data.class);
        Data mockData39 = mock(Data.class);
        Data mockData40 = mock(Data.class);
        Data mockData41 = mock(Data.class);
        Data mockData42 = mock(Data.class);
        Data mockData43 = mock(Data.class);
        Data mockData44 = mock(Data.class);
        Data mockData45 = mock(Data.class);
        Data mockData46 = mock(Data.class);
        Data mockData47 = mock(Data.class);
        Data mockData48 = mock(Data.class);
        Data mockData49= mock(Data.class);
        Data mockData50 = mock(Data.class);
        Data mockData51 = mock(Data.class);
        List<Data>  dataList = new ArrayList<Data>();
        Session mockSession = mock(Session.class);
        Transaction mockTransaction = mock(Transaction.class);
        EntityManagerFactory mockEntityManagerFactory = mock(EntityManagerFactory.class);
        dataList.add(mockData);
        dataList.add(mockData2);
        dataList.add(mockData3);
        dataList.add(mockData4);
        dataList.add(mockData5);
        dataList.add(mockData6);
        dataList.add(mockData7);
        dataList.add(mockData8);
        dataList.add(mockData9);
        dataList.add(mockData10);
        dataList.add(mockData11);
        dataList.add(mockData12);
        dataList.add(mockData13);
        dataList.add(mockData14);
        dataList.add(mockData15);
        dataList.add(mockData16);
        dataList.add(mockData17);
        dataList.add(mockData18);
        dataList.add(mockData19);
        dataList.add(mockData20);
        dataList.add(mockData21);
        dataList.add(mockData22);
        dataList.add(mockData23);
        dataList.add(mockData24);
        dataList.add(mockData25);
        dataList.add(mockData26);
        dataList.add(mockData27);
        dataList.add(mockData28);
        dataList.add(mockData29);
        dataList.add(mockData30);
        dataList.add(mockData31);
        dataList.add(mockData32);
        dataList.add(mockData33);
        dataList.add(mockData34);
        dataList.add(mockData35);
        dataList.add(mockData36);
        dataList.add(mockData37);
        dataList.add(mockData38);
        dataList.add(mockData39);
        dataList.add(mockData40);
        dataList.add(mockData41);
        dataList.add(mockData42);
        dataList.add(mockData43);
        dataList.add(mockData44);
        dataList.add(mockData45);
        dataList.add(mockData46);
        dataList.add(mockData47);
        dataList.add(mockData48);
        dataList.add(mockData49);
        dataList.add(mockData50);
        dataList.add(mockData51);

        when(entityManager.getEntityManagerFactory()).thenReturn(mockEntityManagerFactory);
        when(mockEntityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.unwrap(Session.class)).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();
        doNothing().when(mockSession).flush();
        doNothing().when(mockSession).clear();

        Site mockSite = mock(Site.class);



        List<Data> result = dataServiceImplementation.batchSave(mockSite, dataList);

        verify(mockSession, times(2)).flush();
        verify(mockSession, times(2)).clear();
    }

    @Test
        public void testCountByUserID(){

        User mockUser = mock(User.class);
        Long expectedLong = Integer.toUnsignedLong(123);
        when(mockUser.getId()).thenReturn(expectedLong);

        int exactInt = Math.toIntExact(expectedLong);
        when(mockDataRepository.countByUserID(exactInt)).thenReturn(expectedLong);

        Long result = dataServiceImplementation.countByUserID(mockUser);

        Assert.assertEquals(expectedLong,result);

    }


    @Test
    public void testGetDataDaysBack(){
        Site mockSite = mock(Site.class);
        Data mockData = mock(Data.class);
        Date date = new Date(1566832931493L *1000);
        int delta = 6;
        Date oldestDate = new DateTime(date).minusDays(delta).toDate();
        Set<Data> dataSet = new HashSet<Data>();
        List<Data> dataList = new ArrayList<Data>();
        dataList.add(mockData);
        dataSet.add(mockData);
        when(mockSite.getDataSet()).thenReturn(dataSet);
        when(mockData.getDateTime()).thenReturn(date);
        String mostRecentString = "Most recent date calculated: "+date.toString();
        String oldestDateString = "Oldest date allowed: "+oldestDate.toString();
        String recordString = "Out of 1 records, 1 remain with a delta of 6";

        List<Data> resultList = org.springframework.test.util.ReflectionTestUtils.invokeMethod(dataServiceImplementation, "getDataDaysBack",mockSite,delta);
        verify(logger, times(1)).info(mostRecentString);
        verify(logger, times(1)).info(oldestDateString);
        verify(logger, times(1)).info(recordString);
        Assert.assertEquals(dataList,resultList);

    }

    @Test
        public void testGetHistoricWEEK(){
        Site mockSite = mock(Site.class);
        HistoryUnit time = HistoryUnit.WEEK;
        Data mockData = mock(Data.class);
        Data mockData2 = mock(Data.class);
        Date date = new Date( 1566834777L*1000);
        Date moreThanAWeek = new Date(1565970777L*1000);
        Set<Data> dataSet = new HashSet<Data>();
        List<Data> dataList = new ArrayList<Data>();
        dataSet.add(mockData);
        dataList.add(mockData);
        dataSet.add(mockData2);

        when(mockSite.getDataSet()).thenReturn(dataSet);
        when(mockData.getDateTime()).thenReturn(date);
        when(mockData2.getDateTime()).thenReturn(moreThanAWeek);

        List<Data> resultList = dataServiceImplementation.getHistoric(time,mockSite);

        Assert.assertEquals(dataList,resultList);
    }

    @Test
    public void testGetHistoricLAST_30(){
        Site mockSite = mock(Site.class);
        HistoryUnit time = HistoryUnit.LAST_30;
        Data mockData = mock(Data.class);
        Data mockData2 = mock(Data.class);
        Date date = new Date( 1566834777L*1000);
        Date moreThan30Days = new Date(1563292377L*1000);
        Set<Data> dataSet = new HashSet<Data>();
        List<Data> dataList = new ArrayList<Data>();
        dataSet.add(mockData);
        dataList.add(mockData);
        dataSet.add(mockData2);

        when(mockSite.getDataSet()).thenReturn(dataSet);
        when(mockData.getDateTime()).thenReturn(date);
        when(mockData2.getDateTime()).thenReturn(moreThan30Days);

        List<Data> resultList = dataServiceImplementation.getHistoric(time,mockSite);

        Assert.assertEquals(dataList,resultList);
    }

    @Test
    public void testGetHistoricYEAR(){
        Site mockSite = mock(Site.class);
        HistoryUnit time = HistoryUnit.YEAR;
        Data mockData = mock(Data.class);
        Data mockData2 = mock(Data.class);
        Date date = new Date( 1566834777L*1000);
        Date moreThanAYEAR = new Date(1500220377L*1000);
        Set<Data> dataSet = new HashSet<Data>();
        List<Data> dataList = new ArrayList<Data>();
        dataSet.add(mockData);
        dataList.add(mockData);
        dataSet.add(mockData2);

        when(mockSite.getDataSet()).thenReturn(dataSet);
        when(mockData.getDateTime()).thenReturn(date);
        when(mockData2.getDateTime()).thenReturn(moreThanAYEAR);

        List<Data> resultList = dataServiceImplementation.getHistoric(time,mockSite);

        Assert.assertEquals(dataList,resultList);
    }

    @Test
    public void testGetHistoricALL(){
        Site mockSite = mock(Site.class);
        HistoryUnit time = HistoryUnit.ALL;
        Data mockData = mock(Data.class);
        Data mockData2 = mock(Data.class);
        Date date = new Date( 1566834777L*1000);
        Date moreThanAYEAR = new Date(1500220377L*1000);
        Set<Data> dataSet = new HashSet<Data>();
        List<Data> dataList = new ArrayList<Data>();
        dataSet.add(mockData);
        dataList.add(mockData);
        dataSet.add(mockData2);
        dataList.add(mockData2);

        when(mockSite.getDataSet()).thenReturn(dataSet);
        when(mockData.getDateTime()).thenReturn(date);
        when(mockData2.getDateTime()).thenReturn(moreThanAYEAR);

        List<Data> resultList = dataServiceImplementation.getHistoric(time,mockSite);

        Assert.assertEquals(dataList,resultList);
    }
}
