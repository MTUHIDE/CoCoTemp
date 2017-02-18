package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.UploadHistory;
import space.hideaway.model.User;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.util.HistoryUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RESTService
{

    private final DataServiceImplementation dataServiceImplementation;
    private final DeviceServiceImplementation deviceServiceImplementation;
    private final UserManagementImpl userManagementImpl;
    private final UploadHistoryService uploadHistoryService;


    @Autowired
    public RESTService(
            DeviceServiceImplementation deviceServiceImplementation,
            UserManagementImpl userManagementImpl,
            DataServiceImplementation dataServiceImplementation,
            UploadHistoryService uploadHistoryService)
    {
        this.deviceServiceImplementation = deviceServiceImplementation;
        this.dataServiceImplementation = dataServiceImplementation;
        this.userManagementImpl = userManagementImpl;
        this.uploadHistoryService = uploadHistoryService;
    }

    public InfoCardSerializer populateInfocards()
    {
        User currentLoggedInUser = userManagementImpl.getCurrentLoggedInUser();
        InfoCardSerializer infoCardSerializer = new InfoCardSerializer();
        infoCardSerializer.setDeviceCount(deviceServiceImplementation.countByUserID(currentLoggedInUser));
        infoCardSerializer.setRecordCount(dataServiceImplementation.countByUserID(currentLoggedInUser));
        infoCardSerializer.setUploadCount(uploadHistoryService.countByUserID(currentLoggedInUser));
        return infoCardSerializer;
    }

    public List<Device> populateDevices()
    {
        User currentLoggedInUser = userManagementImpl.getCurrentLoggedInUser();
        ArrayList<Device> deviceList = new ArrayList<>(currentLoggedInUser.getDeviceSet());
        return deviceList.stream().sorted(Comparator.comparing(Device::getDeviceName)).collect(Collectors.toList());
    }

    public List<UploadHistory> getUploadHistory(HistoryUnit historyUnit)
    {
        User currentLoggedInUser = userManagementImpl.getCurrentLoggedInUser();
        switch (historyUnit)
        {
            case WEEK:
                return uploadHistoryService.getLastWeek(currentLoggedInUser);

            case MONTH:
                return uploadHistoryService.getLastMonth(currentLoggedInUser);
            case YEAR:
                return uploadHistoryService.getLastYear(currentLoggedInUser);

        }
        return uploadHistoryService.getLastMonth(currentLoggedInUser);
    }
}
