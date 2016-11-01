package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.User;

import java.util.Set;

/**
 * Created by dough on 11/1/2016.
 */
public interface DashboardService {
    Set<Data> getAllData(User user);

    Data getMaxRecording(User user);

    Data getMinRecording(User user);

    Data getLastWeekAverage(User user);

    Data getLastMonthAverage(User user);

    int getNumberOfRecords(User user);
}
