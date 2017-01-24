package space.hideaway.services;

import space.hideaway.model.Data;

import java.util.List;


public interface DataSavingService
{
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
}
