package space.hideaway.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.FeatureCollection;

/**
 * Created by dough on 11/8/2016.
 */
public class GEOJsonService {

    public String getGeoJsonForLastRecordedTemperature() {
        FeatureCollection features = new FeatureCollection();


        try {
            return new ObjectMapper().writeValueAsString(features);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
