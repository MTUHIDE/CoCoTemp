package space.hideaway;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * HIDE CoCoTemp 2016
 * An instruction class for GSON. Used to map the device validator to a usable JSON tree.
 */
public class DeviceErrorSerializer implements JsonSerializer<DeviceValidator> {

    /**
     * Convert a DeviceValidator instance into a JSON tree.
     *
     * @param src       The DeviceValidator class.
     * @param typeOfSrc Unused.
     * @param context   Unused.
     * @return A JsonObject to be converted to a JSON tree.
     */
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
