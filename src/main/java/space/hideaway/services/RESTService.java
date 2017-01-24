package space.hideaway.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Data;
import space.hideaway.model.Device;

import java.util.List;


@Service
public class RESTService {

    private final DataSavingServiceImplementation dataServiceImplementation;
    private final DeviceServiceImplementation deviceServiceImplementation;

    @Autowired
    public RESTService(
            SecurityServiceImplementation securityServiceImplementation,
            DeviceServiceImplementation deviceServiceImplementation,
            UserManagementImpl userManagementImpl,
            DataSavingServiceImplementation dataServiceImplementation,
            UploadHistoryService uploadHistoryService)
    {
        this.deviceServiceImplementation = deviceServiceImplementation;
        this.dataServiceImplementation = dataServiceImplementation;
    }

    public String getGeoJsonForLastRecordedTemperature() {
        FeatureCollection features = new FeatureCollection();
        List<Data> averageDataForCurrentDay = dataServiceImplementation.getAverageDataForCurrentDay();
        for (Data data : averageDataForCurrentDay) {
            Device device = deviceServiceImplementation.findByKey(data.getDeviceID().toString());
            Feature feature = new Feature();
            Point point = new Point(device.getDeviceLongitude(), device.getDeviceLatitude());
            feature.setProperty("name", device.getDeviceName());
            feature.setProperty("temperature", data.getTemperature());
            feature.setGeometry(point);
            features.add(feature);
        }

        try {
            return new ObjectMapper().writeValueAsString(features);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Something went wrong.";
    }
}
