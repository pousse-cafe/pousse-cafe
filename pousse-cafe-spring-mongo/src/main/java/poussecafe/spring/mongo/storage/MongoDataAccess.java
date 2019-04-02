package poussecafe.spring.mongo.storage;

import java.io.Serializable;
import java.util.List;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;
import poussecafe.runtime.OptimisticLockingException;

public abstract class MongoDataAccess<K, D extends EntityAttributes<K>, M extends Serializable> implements EntityDataAccess<K, D> {

    @Override
    public D findData(K id) {
        return mongoRepository().findById(convertId(id)).orElse(null);
    }

    protected abstract M convertId(K id);

    protected abstract MongoRepository<D, M> mongoRepository();

    @Override
    public void addData(D data) {
        try {
            mongoRepository().insert(data);
        } catch (OptimisticLockingFailureException e) {
            throw translateOptimisticLockingFailure(e);
        }
    }

    private OptimisticLockingException translateOptimisticLockingFailure(OptimisticLockingFailureException e) {
        return new OptimisticLockingException(e);
    }

    @Override
    public void updateData(D data) {
        try {
            mongoRepository().save(data);
        } catch (OptimisticLockingFailureException e) {
            throw translateOptimisticLockingFailure(e);
        }
    }

    @Override
    public void deleteData(K id) {
        try {
            mongoRepository().deleteById(convertId(id));
        } catch (OptimisticLockingFailureException e) {
            throw translateOptimisticLockingFailure(e);
        }
    }

    @Override
    public void deleteAll() {
        mongoRepository().deleteAll();
    }

    @Override
    public List<D> findAll() {
        return mongoRepository().findAll();
    }
}
