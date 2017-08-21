package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.site.Site;
import space.hideaway.model.upload.UploadHistory;
import space.hideaway.model.User;
import space.hideaway.model.json.InfoCardSerializer;
import space.hideaway.services.data.DataServiceImplementation;
import space.hideaway.services.site.SiteServiceImplementation;
import space.hideaway.services.upload.UploadHistoryService;
import space.hideaway.services.user.UserServiceImplementation;
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
        return deviceList.stream().sorted(Comparator.comparing(Device::getType)).collect(Collectors.toList());
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
}
