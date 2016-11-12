package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.List;
import java.util.Set;

/**
 * Created by dough on 11/1/2016.
 */
public interface DataService {
    void save(Data data);

    List<Data> batchSave(List<Data> dataList);

    List<Data> getAverageDataForCurrentDay();

    Data getLastRecording(Device device);

    Set<Data> getAllData(User user);
}
