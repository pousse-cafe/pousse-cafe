package poussecafe.data.memory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import poussecafe.storable.StorableData;

public class InMemoryDataJsonDeserializer<D extends StorableData<?>> implements JsonDeserializer<D> {

    @Override
    public D deserialize(JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context)
                    throws JsonParseException {
        if (json.equals(JsonNull.INSTANCE)) {
            return null;
        }

        @SuppressWarnings("unchecked")
        D object = InMemoryDataUtils.newDataImplementation((Class<D>) typeOfT);
        InMemoryDataImplementation dataImplementation = InMemoryDataUtils.getDataImplementation(object);
        JsonObject jsonObject = (JsonObject) json;
        for (Property property : dataImplementation.getProperties()) {
            JsonElement element = jsonObject.get(property.getName());
            if (element instanceof JsonObject) {
                JsonObject serializedObject = (JsonObject) element;
                String className = serializedObject.get("_class").getAsString();
                try {
                    dataImplementation.set(property.getName(), context.deserialize(element, Class.forName(className)));
                } catch (ClassNotFoundException e) {
                    throw new InMemoryDataException(
                            "Unable to unserizalize a property value, class " + className + " not found", e);
                }
            } else {
                dataImplementation.set(property.getName(), context.deserialize(element, property.getType()));
            }
        }
        return object;
    }

}
