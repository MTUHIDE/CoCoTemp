package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

/**
 * Created by dough on 11/1/2016.
 */
public interface DashboardService {

    Data getLastRecording(Device device);

    Data getMaxRecording(User user);

    Data getMinRecording(User user);

    Data getLastWeekAverage(User user);

    Data getLastMonthAverage(User user);

    int getNumberOfRecords(User user);
}
