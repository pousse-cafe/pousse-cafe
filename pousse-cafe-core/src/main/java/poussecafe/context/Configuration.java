package poussecafe.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import poussecafe.exception.PousseCafeException;

public class Configuration {

    private Map<String, Object> values = new HashMap<>();

    void add(String key,
            Object value) {
        if(values.containsKey(key)) {
            throw new PousseCafeException("Key " + key + " has already a value");
        }
        if(value == null) {
            throw new PousseCafeException("Value cannot be null");
        }
        values.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> value(String key) {
        return Optional.ofNullable((T) values.get(key));
    }
}
