package poussecafe.storable;

public interface IdentifiedStorableDataAccess<D extends StorableData> {

    D findData(Object key);

    void addData(D data);

    void updateData(D data);

    void deleteData(Object key);
}
