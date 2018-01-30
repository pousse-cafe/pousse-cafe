package poussecafe.inmemory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import poussecafe.storable.IdentifiedStorableDataAccess;
import poussecafe.storable.StorableData;

public abstract class InMemoryDataAccess<D extends StorableData> implements IdentifiedStorableDataAccess<D> {

    private Map<Object, byte[]> storage;

    public InMemoryDataAccess() {
        storage = new HashMap<>();
    }

    @Override
    public synchronized D findData(Object key) {
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
        Object key = extractKey(data);
        if (storage.containsKey(key)) {
            throw new InMemoryDataException("Duplicate key");
        }
        storage.put(key, serialize(data));
    }

    protected abstract Object extractKey(D data);

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
    public synchronized void updateData(D data) {
        Object key = extractKey(data);
        if (!storage.containsKey(key)) {
            throw new InMemoryDataException("No entry with key " + key);
        }
        storage.put(key, serialize(data));
    }

    @Override
    public synchronized void deleteData(Object key) {
        storage.remove(key);
    }

}
