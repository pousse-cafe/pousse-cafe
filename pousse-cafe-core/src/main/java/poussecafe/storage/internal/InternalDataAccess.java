package poussecafe.storage.internal;

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
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;
import poussecafe.exception.PousseCafeException;
import poussecafe.runtime.DuplicateKeyException;
import poussecafe.storage.internal.uniqueindex.AdditionPlan;
import poussecafe.storage.internal.uniqueindex.Plan;
import poussecafe.storage.internal.uniqueindex.UniqueIndex;
import poussecafe.storage.internal.uniqueindex.UpdatePlan;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class InternalDataAccess<K, D extends EntityAttributes<K>> implements EntityDataAccess<K, D> {

    private Map<K, byte[]> storage;

    private Multimap<Object, K> index = new Multimap<>();

    private Map<String, UniqueIndex<D>> uniqueIndexes = new HashMap<>();

    private Optional<OptimisticLocker> locker = Optional.empty();

    public InternalDataAccess() {
        storage = new HashMap<>();
    }

    @Override
    public synchronized D findData(K id) {
        byte[] serializedBytes = storage.get(id);
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
            throw new InternalStorageException("Unable to deserialize data", e);
        }
    }

    @Override
    public synchronized void addData(D data) {
        K id = data.identifier().value();
        if (storage.containsKey(id)) {
            throw new DuplicateKeyException("Duplicate id " + id);
        }
        List<AdditionPlan> additionPlans = prepareAddition(data);
        if(locker.isPresent()) {
            locker.get().initializeVersion(data);
        }
        storage.put(id, serialize(data));
        index(data);
        commitPlans(additionPlans);
    }

    private List<AdditionPlan> prepareAddition(D data) {
        return uniqueIndexes.values().stream()
            .map(uniqueIndex -> uniqueIndex.prepareAddition(data))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());
    }

    private void index(D data) {
        K id = data.identifier().value();
        List<Object> indexedData = extractIndexedData(data);
        indexedData.forEach(indexed -> {
            if(indexed != null) {
                index.put(indexed, id);
            }
        });
    }

    private void commitPlans(List<? extends Plan> plans) {
        plans.forEach(Plan::commit);
    }

    protected List<Object> extractIndexedData(D data) { // NOSONAR
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
            throw new InternalStorageException("Unable to serialize data", e);
        }
    }

    @Override
    public synchronized void updateData(D newData) {
        K id = newData.identifier().value();
        if (!storage.containsKey(id)) {
            throw new InternalStorageException("No entry with id " + id);
        }
        D oldData = findData(id);

        List<UpdatePlan> updatePlans = prepareUpdate(Optional.ofNullable(oldData), newData);
        unindex(oldData);
        if(locker.isPresent()) {
            long actualVersion = locker.get().getVersion(oldData).orElseThrow(InternalStorageException::new);
            locker.get().ensureAndIncrement(actualVersion, newData);
        }
        storage.put(id, serialize(newData));
        index(newData);
        commitPlans(updatePlans);
    }

    private List<UpdatePlan> prepareUpdate(Optional<D> oldData,
            D newData) {
        return uniqueIndexes.values().stream()
                .map(uniqueIndex -> uniqueIndex.prepareUpdate(oldData, newData))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    @Override
    public synchronized void deleteData(K id) {
        D data = findData(id);
        if(data != null) {
            storage.remove(id);
            unindex(data);
        }
    }

    private synchronized void unindex(D data) {
        K id = data.identifier().value();
        List<Object> indexedData = extractIndexedData(data);
        indexedData.forEach(indexed -> index.remove(indexed, id));
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

    protected void versionField(String versionField) {
        if(locker.isPresent()) {
            throw new PousseCafeException("Optistic locker has already been configured");
        }
        locker = Optional.of(new OptimisticLocker(versionField));
    }
}
