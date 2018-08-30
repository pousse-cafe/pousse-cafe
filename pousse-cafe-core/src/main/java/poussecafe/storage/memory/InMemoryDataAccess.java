package poussecafe.storage.memory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import poussecafe.collection.Multimap;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.IdentifiedStorableDataAccess;
import poussecafe.storage.memory.uniqueindex.AdditionPlan;
import poussecafe.storage.memory.uniqueindex.Plan;
import poussecafe.storage.memory.uniqueindex.UniqueIndex;
import poussecafe.storage.memory.uniqueindex.UpdatePlan;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class InMemoryDataAccess<K, D extends IdentifiedStorableData<K>> implements IdentifiedStorableDataAccess<K, D> {

    private Map<K, byte[]> storage;

    private Multimap<Object, K> index = new Multimap<>();

    private Map<String, UniqueIndex<D>> uniqueIndexes = new HashMap<>();

    public InMemoryDataAccess() {
        storage = new HashMap<>();
    }

    @Override
    public synchronized D findData(K key) {
        byte[] serializedBytes = storage.get(key);
        if (serializedBytes == null) {
            return null;
        } else {
            return deserialize(serializedBytes);
        }
    }

    @SuppressWarnings("unchecked")
    private D deserialize(byte[] serializedBytes) {
        try {
            return (D) new ObjectInputStream(new ByteArrayInputStream(serializedBytes)).readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new InMemoryDataException("Unable to deserialize data", e);
        }
    }

    @Override
    public synchronized void addData(D data) {
        K key = data.key().get();
        if (storage.containsKey(key)) {
            throw new InMemoryDataException("Duplicate key");
        }
        List<AdditionPlan> additionPlans = prepareAddition(data);
        storage.put(key, serialize(data));
        index(data);
        commitPlans(additionPlans);
    }

    private List<AdditionPlan> prepareAddition(D data) {
        return uniqueIndexes.values().stream()
            .map(index -> index.prepareAddition(data))
            .collect(toList());
    }

    private void index(D data) {
        K key = data.key().get();
        List<Object> indexedData = extractIndexedData(data);
        indexedData.forEach(indexed -> {
            if(indexed != null) {
                index.put(indexed, key);
            }
        });
    }

    private void commitPlans(List<? extends Plan> plans) {
        plans.forEach(Plan::commit);
    }

    protected List<Object> extractIndexedData(D data) {
        return emptyList();
    }

    private byte[] serialize(D data) {
        try {
            ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bytesBuffer);
            oos.writeObject(data);
            oos.flush();
            return bytesBuffer.toByteArray();
        } catch (IOException e) {
            throw new InMemoryDataException("Unable to serialize data", e);
        }
    }

    @Override
    public synchronized void updateData(D newData) {
        K key = newData.key().get();
        if (!storage.containsKey(key)) {
            throw new InMemoryDataException("No entry with key " + key);
        }
        D oldData = findData(key);

        List<UpdatePlan> updatePlans = prepareUpdate(Optional.ofNullable(oldData), newData);
        unindex(oldData);
        storage.put(key, serialize(newData));
        index(newData);
        commitPlans(updatePlans);
    }

    private List<UpdatePlan> prepareUpdate(Optional<D> oldData,
            D newData) {
        return uniqueIndexes.values().stream()
                .map(index -> index.prepareUpdate(oldData, newData))
                .collect(toList());
    }

    @Override
    public synchronized void deleteData(K key) {
        D data = findData(key);
        if(data != null) {
            storage.remove(key);
            unindex(data);
        }
    }

    private synchronized void unindex(D data) {
        K key = data.key().get();
        List<Object> indexedData = extractIndexedData(data);
        indexedData.forEach(indexed -> index.remove(indexed, key));
    }

    protected synchronized List<D> findBy(Object indexed) {
        return new ArrayList<>(index.get(indexed).stream().map(this::findData).collect(toList()));
    }

    @Override
    public synchronized void deleteAll() {
        storage.clear();
        index.clear();
    }

    protected synchronized List<D> findByPredicate(Predicate<D> predicate) {
        return streamAll().filter(predicate).collect(toList());
    }

    public synchronized Stream<D> streamAll() {
        return storage.values().stream().map(this::deserialize);
    }

    @Override
    public synchronized List<D> findAll() {
        return streamAll().collect(toList());
    }

    protected void registerUniqueIndex(UniqueIndex<D> uniqueIndex) {
        if(uniqueIndexes.containsKey(uniqueIndex.name())) {
            throw new IllegalArgumentException("A unique index with name " + uniqueIndex.name() + " already exists");
        }
        uniqueIndexes.put(uniqueIndex.name(), uniqueIndex);
    }
}
