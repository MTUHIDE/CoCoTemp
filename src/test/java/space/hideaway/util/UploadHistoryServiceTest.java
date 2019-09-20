package space.hideaway.util;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import space.hideaway.model.User;
import space.hideaway.model.upload.UploadHistory;
import space.hideaway.repositories.UploadHistoryRepository;
import space.hideaway.services.upload.UploadHistoryService;
import space.hideaway.services.upload.UploadHistoryServiceImplementation;

import java.util.*;

import static org.mockito.Mockito.*;

public class UploadHistoryServiceTest {


    UploadHistoryService uploadHistoryService ;
    UploadHistoryRepository mockUploadHistoryRepository;
    UploadHistoryService spyUploadHistoryService;


    @Before
    public void setUp(){
        mockUploadHistoryRepository = mock(UploadHistoryRepository.class);
        uploadHistoryService = new UploadHistoryServiceImplementation(mockUploadHistoryRepository);
        spyUploadHistoryService = spy(uploadHistoryService);
    }

    @Test
    public void testSave(){
        UploadHistory mockUploadHistory = mock(UploadHistory.class);
        when(mockUploadHistoryRepository.save(mockUploadHistory)).thenReturn(mockUploadHistory);

        Assert.assertEquals(mockUploadHistory,uploadHistoryService.save(mockUploadHistory));

    }

    @Test
    public void testSetViewed(){
        String UUIDString = "91814557-61e9-41e0-8f1c-7de69b637ced";
        UUID  expectedUUID = UUID.fromString(UUIDString);
        UploadHistory mockUploadHistory = mock(UploadHistory.class);
        when(mockUploadHistoryRepository.getOne(expectedUUID)).thenReturn(mockUploadHistory);
        when(mockUploadHistoryRepository.save(mockUploadHistory)).thenReturn(mockUploadHistory);

        doNothing().when(mockUploadHistory).setViewed(true);
        Assert.assertEquals(mockUploadHistory,uploadHistoryService.setViewed(expectedUUID));

    }

    @Test
    public void testSavePending(){
        String UUIDString = "91814557-61e9-41e0-8f1c-7de69b637ced";
        UUID  expectedUUID = UUID.fromString(UUIDString);
        int expectedUserId = 17;
        boolean expectederror = false;
        Long  expectedLong = new Long(234);
        String expectedMessage = "msg";
        int expectedRecords = 7;
        ArgumentCaptor<UploadHistory> argument = ArgumentCaptor.forClass(UploadHistory.class);

        UploadHistory actualHistory = spyUploadHistoryService.savePending(expectedUUID,expectedUserId,expectederror,expectedLong,expectedMessage,expectedRecords);

        verify(spyUploadHistoryService).save(argument.capture());


        Assert.assertEquals(expectedUUID,argument.getValue().getSiteID());
        Assert.assertEquals(expectedUserId,argument.getValue().getUserID());
        Assert.assertEquals(expectederror,argument.getValue().isError());
        Assert.assertEquals(expectedLong,argument.getValue().getDuration());
        Assert.assertEquals(expectedMessage,argument.getValue().getDescription());
        Assert.assertEquals(expectedRecords,argument.getValue().getRecords());
    }
    @Test
    public void testSaveFinished(){
        UploadHistory mockUploadHistory = new UploadHistory();
        boolean expectedError = false;
        Long expectedDuration = new Long(23);
        int expectedSize = 12;
        String expectedMsg = "msg";

        when(spyUploadHistoryService.save(mockUploadHistory)).thenReturn(mockUploadHistory);

        UploadHistory actualHistory = spyUploadHistoryService.saveFinished(mockUploadHistory,expectedError,expectedDuration,expectedSize,expectedMsg);

        Assert.assertEquals(expectedError,actualHistory.isError());
        Assert.assertEquals(expectedDuration,actualHistory.getDuration());
        Assert.assertEquals(expectedSize,actualHistory.getRecords());
        Assert.assertEquals(expectedMsg,actualHistory.getDescription());

    }

    @Test
    public void testCountByUserId(){
        User mockUser = mock(User.class);
        Long expectedId = new Long(345);
        when(mockUser.getId()).thenReturn(expectedId);
        int expectedInt  = Math.toIntExact(expectedId);

        when(mockUploadHistoryRepository.countByUserID(expectedInt)).thenReturn(expectedId);

        Assert.assertEquals((long)expectedId,uploadHistoryService.countByUserID(mockUser));
    }

    @Test
    public void testGetLastWeek(){
        UploadHistory mockUploadHistory = mock(UploadHistory.class);
        List<UploadHistory> historyList = new ArrayList<UploadHistory>();
        historyList.add(mockUploadHistory);

        User mockUser = mock(User.class);
        Long expectedId = new Long(345);
        when(mockUser.getId()).thenReturn(expectedId);
        int expectedInt  = Math.toIntExact(expectedId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date date = calendar.getTime();

        when(mockUploadHistoryRepository.getHistoric(date,expectedInt)).thenReturn(historyList);

        Assert.assertEquals(historyList,uploadHistoryService.getLastWeek(mockUser));



    }



}
