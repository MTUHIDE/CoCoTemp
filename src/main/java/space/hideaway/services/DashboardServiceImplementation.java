package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.Set;

/**
 * Created by dough on 11/1/2016.
 */
public class DashboardServiceImplementation implements DashboardService {

    @Override
    public Set<Data> getAllData(User user) {
        return null;
    }

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
    public int getNumberOfRecords(User user) {
        int size = 0;
        for (Device device : user.getDeviceSet()) {
            size += device.getDataSet().size();
        }
        return size;
    }
}
