package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.User;


public interface DashboardService {
    Data getMaxRecording(User user);
    Data getMinRecording(User user);
    Data getLastWeekAverage(User user);
    Data getLastMonthAverage(User user);
    int getNumberOfRecords(User user);
}
