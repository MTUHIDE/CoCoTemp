package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

/**
 * Created by dough on 11/1/2016.
 */
@Service
public class DashboardServiceImplementation implements DashboardService {

    @Autowired
    DeviceServiceImplementation deviceServiceImplementation;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DataService dataService;

    @Override
    public Data getMaxRecording(User user) {
        return null;
    }

    @Override
    public Data getMinRecording(User user) {
        return null;
    }

    @Override
    public Data getLastWeekAverage(User user) {
        return null;
    }

    @Override
    public Data getLastMonthAverage(User user) {
        return null;
    }

    @Override
    @Transactional
    public int getNumberOfRecords(User user) {
        int size = 0;
        for (Device device : user.getDeviceSet()) {
            size += device.getDataSet().size();
        }
        return size;
    }
}
