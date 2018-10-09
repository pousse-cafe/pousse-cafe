package poussecafe.domain;

import java.util.List;

public interface EntityDataAccess<K, D extends EntityData<K>> {

    D findData(K key);

    void addData(D data);

    void updateData(D data);

    void deleteData(K key);

    void deleteAll();

    List<D> findAll();
}
