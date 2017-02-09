package poussecafe.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import poussecafe.exception.PousseCafeException;

public class PolymorphicTypeDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context)
                    throws JsonParseException {
        if (json.equals(JsonNull.INSTANCE)) {
            return null;
        }

        JsonObject jsonObject = (JsonObject) json;
        String className = jsonObject.get("_class").getAsString();
        Class<?> classOfT;
        try {
            classOfT = Class.forName(className);
            return context.deserialize(jsonObject, classOfT);
        } catch (ClassNotFoundException e) {
            throw new PousseCafeException("Unable to deserialize object", e);
        }
    }
}
