package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.InfoCardSerializer;
import space.hideaway.model.User;


@Service
public class RESTService {

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
}
