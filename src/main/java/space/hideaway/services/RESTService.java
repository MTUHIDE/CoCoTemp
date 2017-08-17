package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.Site;
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
    private final SiteServiceImplementation siteServiceImplementation;
    private final UserServiceImplementation userServiceImplementation;
    private final UploadHistoryService uploadHistoryService;
    private final DeviceService deviceService;

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

    public List<Site> populateSites()
    {
        User currentLoggedInUser = userServiceImplementation.getCurrentLoggedInUser();
        ArrayList<Site> siteList = new ArrayList<>(currentLoggedInUser.getSiteSet());
        return siteList.stream().sorted(Comparator.comparing(Site::getSiteName)).collect(Collectors.toList());
    }

    public List<Device> populateDevices(){
        User currentLoggedInUser = userServiceImplementation.getCurrentLoggedInUser();
        ArrayList<Device> deviceList = new ArrayList<>(currentLoggedInUser.getDeviceSet());
        return deviceList.stream().sorted(Comparator.comparing(Device::getType)).collect(Collectors.toList());
    }

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
}
