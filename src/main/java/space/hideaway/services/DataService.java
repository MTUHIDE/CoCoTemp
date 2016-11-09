package space.hideaway.services;

import space.hideaway.model.Data;

import java.util.List;

/**
 * Created by dough on 11/1/2016.
 */
public interface DataService {
    void save(Data data);

    List<Data> batchSave(List<Data> dataList);

    List<Data> getAverageDataForCurrentDay();
}
