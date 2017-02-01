package poussecafe.data.memory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import poussecafe.storable.StorableData;

public class InMemoryDataJsonSerializer<D extends StorableData<?>> implements JsonSerializer<D> {

    @Override
    public JsonElement serialize(D object,
            Type typeOfSrc,
            JsonSerializationContext context) {
        InMemoryDataImplementation dataImplementation = InMemoryDataUtils.getDataImplementation(object);
        JsonObject jsonObject = new JsonObject();
        for (Property property : dataImplementation.getProperties()) {
            addJsonProperty(jsonObject, property, dataImplementation, context);
        }
        return jsonObject;
    }

    private void addJsonProperty(JsonObject jsonObject,
            Property property,
            InMemoryDataImplementation dataImplementation,
            JsonSerializationContext context) {
        Object value = dataImplementation.get(property.getName());
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            jsonObject.addProperty(property.getName(), (String) value);
        } else if (value instanceof Number) {
            jsonObject.addProperty(property.getName(), (Number) value);
        } else if (value instanceof Boolean) {
            jsonObject.addProperty(property.getName(), (Boolean) value);
        } else if (value instanceof Character) {
            jsonObject.addProperty(property.getName(), (Character) value);
        } else {
            addObjectProperty(jsonObject, property, context, value);
        }
    }

    protected void addObjectProperty(JsonObject jsonObject,
            Property property,
            JsonSerializationContext context,
            Object value) {
        JsonElement element = context.serialize(value);
        if (element instanceof JsonObject) {
            JsonObject serializedObject = (JsonObject) element;
            serializedObject.addProperty("_class", value.getClass().getCanonicalName());
        }
        jsonObject.add(property.getName(), element);
    }

}
