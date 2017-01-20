package space.hideaway;

import com.google.gson.*;
import space.hideaway.validation.DeviceValidator;

import java.lang.reflect.Type;


public class DeviceErrorSerializer implements JsonSerializer<DeviceValidator> {
    @Override
    public JsonElement serialize(DeviceValidator src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("error", new JsonPrimitive(src.hasErrors()));
        if (src.hasErrors()) {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(src.getErrors());
            result.add("errors", jsonElement);
        }
        return result;
    }
}
