package poussecafe.storable;

public interface IdentifiedStorableDataAccess<K, D extends IdentifiedStorableData<K>> {

    D findData(K key);

    void addData(D data);

    void updateData(D data);

    void deleteData(K key);
}
