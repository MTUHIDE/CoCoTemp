package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.List;
import java.util.Set;


public interface DataService {
    /**
     * Save a new data point into the database.
     *
     * @param data The new data point to be saved.
     */
    void save(Data data);

    /**
     * Batch save a large amount of data points.
     *
     * @param dataList A list of points to be saved.
     * @return The newly saved list.
     */
    List<Data> batchSave(List<Data> dataList);

    /**
     * Get a list of average temperature points for each device on the current day.
     *
     * @return A list of points, each representing the average recorded point for the current day.
     */
    List<Data> getAverageDataForCurrentDay();

    /**
     * Get the most current record for a given device.
     *
     * @param device The device to obtain the most recent point for.
     * @return The most current point for a given device. If no point is found, a dummy record is returned
     * with extreme values.
     */
    Data getMostCurrentRecord(Device device);

    /**
     * Get a list of data points for a user.
     *
     * @param user The user to obtain a list of data points for.
     * @return A list of data points for the given user.
     */
    Set<Data> getAllData(User user);
}
