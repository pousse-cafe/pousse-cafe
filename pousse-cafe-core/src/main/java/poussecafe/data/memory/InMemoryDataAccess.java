package poussecafe.data.memory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;

public class InMemoryDataAccess<K, D extends StorableData<K>> implements StorableDataAccess<K, D> {

    private Map<K, String> storage;

    private Class<D> dataType;

    private Gson gson;

    public InMemoryDataAccess(Class<D> dataType) {
        storage = new HashMap<>();
        this.dataType = dataType;
        gson = new GsonBuilder()
                .registerTypeAdapter(dataType,
                        (InstanceCreator<D>) type -> InMemoryDataUtils.newDataImplementation(dataType))
                .registerTypeAdapter(dataType, new InMemoryDataJsonSerializer<>())
                .registerTypeAdapter(dataType, new InMemoryDataJsonDeserializer<>())
                .create();
    }

    @Override
    public synchronized D findData(K key) {
        String json = storage.get(key);
        if (json == null) {
            return null;
        } else {
            return deserialize(json);
        }
    }

    private D deserialize(String json) {
        return gson.fromJson(json, dataType);
    }

    @Override
    public synchronized void addData(D data) {
        if (storage.containsKey(data.getKey())) {
            throw new InMemoryDataException("Duplicate key");
        }
        storage.put(data.getKey(), gson.toJson(data, dataType));
    }

    @Override
    public synchronized void updateData(D data) {
        if (!storage.containsKey(data.getKey())) {
            throw new InMemoryDataException("No entry with key " + data.getKey());
        }
        storage.put(data.getKey(), gson.toJson(data, dataType));
    }

    @Override
    public synchronized void deleteData(K key) {
        storage.remove(key);
    }

    public synchronized List<D> findByJSONPath(JSONPathSpecification specification) {
        List<D> found = new ArrayList<>();
        for (String json : storage.values()) {
            if (specification.matches(json)) {
                found.add(deserialize(json));
            }
        }
        return found;
    }

    public void addRawData(JsonElement value) {
        D data = deserialize(value.toString());
        addData(data);
    }

}
