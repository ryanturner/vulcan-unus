package co.vulcanus.dux.model.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import co.vulcanus.dux.model.DeviceState;

/**
 * Created by ryan_turner on 10/19/15.
 */
public class DeviceStateSerializer implements JsonSerializer<DeviceState> {
    public JsonElement serialize(final DeviceState deviceState, final Type type, final JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("2", new JsonPrimitive(deviceState.getPinValue(2)));
        result.add("3", new JsonPrimitive(deviceState.getPinValue(3)));
        result.add("4", new JsonPrimitive(deviceState.getPinValue(4)));
        result.add("5", new JsonPrimitive(deviceState.getPinValue(5)));
        result.add("6", new JsonPrimitive(deviceState.getPinValue(6)));
        result.add("7", new JsonPrimitive(deviceState.getPinValue(7)));
        result.add("8", new JsonPrimitive(deviceState.getPinValue(8)));
        result.add("9", new JsonPrimitive(deviceState.getPinValue(9)));
        return result;
    }
}
