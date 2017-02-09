package poussecafe.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class PolymorphicTypeSerializer<T> implements JsonSerializer<T> {

    @Override
    public JsonElement serialize(T object,
            Type typeOfSrc,
            JsonSerializationContext context) {
        JsonElement element = context.serialize(object);
        JsonObject serializedObject = (JsonObject) element;
        serializedObject.addProperty("_class", object.getClass().getCanonicalName());
        return element;
    }

}
