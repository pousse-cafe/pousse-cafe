package poussecafe.storable;

public interface StorableDataAccess<K, D extends StorableData<K>> {

    D findData(K key);

    void addData(D data);

    void updateData(D data);

    void deleteData(K key);
}
