package poussecafe.spring.mongo.storage;

import java.io.Serializable;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.IdentifiedStorableDataAccess;

public abstract class MongoDataAccess<K, D extends IdentifiedStorableData<K>, M extends Serializable> implements IdentifiedStorableDataAccess<K, D> {

    @Override
    public D findData(K key) {
        return mongoRepository().findById(convertKey(key)).orElse(null);
    }

    protected abstract M convertKey(K key);

    protected abstract MongoRepository<D, M> mongoRepository();

    @Override
    public void addData(D data) {
        mongoRepository().insert(data);
    }

    @Override
    public void updateData(D data) {
        mongoRepository().save(data);
    }

    @Override
    public void deleteData(K key) {
        mongoRepository().deleteById(convertKey(key));
    }

    @Override
    public void deleteAll() {
        mongoRepository().deleteAll();
    }
}
