package poussecafe.domain;

import java.util.List;

public interface EntityDataAccess<K, D extends EntityAttributes<K>> {

    D findData(K id);

    void addData(D data);

    void updateData(D data);

    void deleteData(K id);

    void deleteAll();

    List<D> findAll();

    boolean existsById(K id);
}
