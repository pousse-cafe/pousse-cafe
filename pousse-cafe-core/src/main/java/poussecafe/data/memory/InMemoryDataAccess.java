package poussecafe.data.memory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import java.util.HashMap;
import java.util.Map;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;

public class InMemoryDataAccess<K, D extends StorableData<K>> implements StorableDataAccess<K, D> {

    private Map<K, String> storage;

    private Class<D> dataType;

    private Gson json;

    public InMemoryDataAccess(Class<D> dataType) {
        storage = new HashMap<>();
        this.dataType = dataType;
        json = new GsonBuilder()
                .registerTypeAdapter(dataType,
                        (InstanceCreator<D>) type -> InMemoryDataUtils.newDataImplementation(dataType))
                .registerTypeAdapter(dataType, new InMemoryDataJsonSerializer<>())
                .registerTypeAdapter(dataType, new InMemoryDataJsonDeserializer<>())
                .create();
    }

    @Override
    public synchronized D findData(K key) {
        return json.fromJson(storage.get(key), dataType);
    }

    @Override
    public synchronized void addData(D data) {
        if (storage.containsKey(data.getKey())) {
            throw new InMemoryDataException("Duplicate key");
        }
        storage.put(data.getKey(), json.toJson(data, dataType));
    }

    @Override
    public synchronized void updateData(D data) {
        if (!storage.containsKey(data.getKey())) {
            throw new InMemoryDataException("No entry with key " + data.getKey());
        }
        storage.put(data.getKey(), json.toJson(data, dataType));
    }

    @Override
    public synchronized void deleteData(K key) {
        storage.remove(key);
    }

}
