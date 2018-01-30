package poussecafe.inmemory;

import java.util.HashMap;
import java.util.Map;
import poussecafe.storable.Property;

public class GenericInMemoryData {

    private Map<String, Object> data = new HashMap<>();

    public void setProperty(String name, Object value) {
        data.put(name, value);
    }

    protected <T> Property<T> property(Class<T> valueClass, String name) {
        return new GenericProperty<>(data, name, valueClass);
    }
}
