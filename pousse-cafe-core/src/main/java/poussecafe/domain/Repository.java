package poussecafe.domain;

import java.util.List;
import java.util.Objects;
import poussecafe.environment.EntityFactory;
import poussecafe.environment.NewEntityInstanceSpecification;
import poussecafe.exception.NotFoundException;
import poussecafe.storage.MessageCollection;

import static java.util.stream.Collectors.toList;

public abstract class Repository<A extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> {

    @SuppressWarnings("unchecked")
    public void setEntityClass(Class<?> entityClass) {
        Objects.requireNonNull(entityClass);
        this.entityClass = (Class<A>) entityClass;
    }

    private Class<A> entityClass;

    public Class<A> entityClass() {
        return entityClass;
    }

    public A find(K key) {
        checkKey(key);
        D data = dataAccess.findData(key);
        return wrap(data);
    }

    private EntityDataAccess<K, D> dataAccess;

    private void checkKey(K key) {
        Objects.requireNonNull(key);
    }

    protected A wrap(D data) {
        if(data == null) {
            return null;
        } else {
            return componentFactory.newEntity(new NewEntityInstanceSpecification.Builder<A>()
                    .entityClass(entityClass)
                    .existingData(data)
                    .build());
        }
    }

    private EntityFactory componentFactory;

    public A get(K key) {
        A entity = find(key);
        if (entity == null) {
            throw new NotFoundException("Entity with key " + key + " not found");
        } else {
            return entity;
        }
    }

    public void add(A entity) {
        checkEntity(entity);
        entity.onAdd();
        addData(entity);
    }

    protected void addData(A entity) {
        MessageCollection messageCollection = entity.messageCollection();
        if(!entity.dontPersist()) {
            D data = entity.attributes();
            dataAccess.addData(data);
        }
        considerMessageSendingAfterAdd(entity, messageCollection);
    }

    private void considerMessageSendingAfterAdd(A entity,
            MessageCollection messageCollection) {
        entity.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    private void checkEntity(A entity) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(entity.attributes());
    }

    public void update(A entity) {
        checkEntity(entity);
        entity.onUpdate();
        updateData(entity);
    }

    protected void updateData(A entity) {
        MessageCollection messageCollection = entity.messageCollection();
        if(!entity.dontPersist()) {
            D data = entity.attributes();
            dataAccess.updateData(data);
        }
        considerMessageSendingAfterUpdate(entity, messageCollection);
    }

    private void considerMessageSendingAfterUpdate(A entity,
            MessageCollection messageCollection) {
        entity.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    public void delete(K key) {
        checkKey(key);
        A entity = find(key);
        if (entity != null) {
            delete(entity);
        }
    }

    public void delete(A entity) {
        entity.onDelete();
        MessageCollection messageCollection = entity.messageCollection();
        dataAccess.deleteData(entity.attributes().key().value());
        considerMessageSendingAfterDelete(entity, messageCollection);
    }

    private void considerMessageSendingAfterDelete(A entity,
            MessageCollection messageCollection) {
        entity.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    protected List<A> wrap(List<D> data) {
        return data.stream().map(this::wrap).filter(Objects::nonNull).collect(toList());
    }

    @SuppressWarnings("unchecked")
    public void setDataAccess(Object dataAccess) {
        Objects.requireNonNull(dataAccess);
        this.dataAccess = (EntityDataAccess<K, D>) dataAccess;
    }

    public EntityDataAccess<K, D> dataAccess() {
        return dataAccess;
    }
}
