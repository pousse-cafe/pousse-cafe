package poussecafe.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public void setMessageCollectionValidator(MessageCollectionValidator messageCollectionValidator) {
        this.messageCollectionValidator = messageCollectionValidator;
    }

    private MessageCollectionValidator messageCollectionValidator;

    /**
     * @deprecated use getOptional instead.
     */
    @Deprecated(since = "0.8.0")
    public A find(K id) {
        return getOptional(id).orElse(null);
    }

    public Optional<A> getOptional(K id) {
        checkId(id);
        D data = dataAccess.findData(id);
        return wrapNullable(data);
    }

    private EntityDataAccess<K, D> dataAccess;

    private void checkId(K id) {
        Objects.requireNonNull(id);
    }

    /**
     * @deprecated use wrapNullable instead.
     */
    @Deprecated(since = "0.8.0")
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

    protected Optional<A> wrapNullable(D data) {
        if(data == null) {
            return Optional.empty();
        } else {
            return Optional.of(componentFactory.newEntity(new NewEntityInstanceSpecification.Builder<A>()
                    .entityClass(entityClass)
                    .existingData(data)
                    .build()));
        }
    }

    private EntityFactory componentFactory;

    public A get(K id) {
        return getOptional(id)
                .orElseThrow(() -> new NotFoundException("Entity with id " + id + " not found"));
    }

    public void add(A entity) {
        checkEntity(entity);
        entity.onAdd();
        addData(entity);
    }

    protected void addData(A entity) {
        MessageCollection messageCollection = entity.messageCollection();
        if(!entity.dontPersist()) {
            messageCollectionValidator.validate(messageCollection);
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
            messageCollectionValidator.validate(messageCollection);
            D data = entity.attributes();
            dataAccess.updateData(data);
        }
        considerMessageSendingAfterUpdate(entity, messageCollection);
    }

    private void considerMessageSendingAfterUpdate(A entity,
            MessageCollection messageCollection) {
        entity.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    public void delete(K id) {
        checkId(id);
        Optional<A> entity = getOptional(id);
        if (entity.isPresent()) {
            delete(entity.get());
        }
    }

    public void delete(A entity) {
        entity.onDelete();
        MessageCollection messageCollection = entity.messageCollection();
        messageCollectionValidator.validate(messageCollection);
        dataAccess.deleteData(entity.attributes().identifier().value());
        considerMessageSendingAfterDelete(entity, messageCollection);
    }

    private void considerMessageSendingAfterDelete(A entity,
            MessageCollection messageCollection) {
        entity.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    protected List<A> wrap(List<D> data) {
        return data.stream().map(this::wrapNullable).filter(Optional::isPresent).map(Optional::get).collect(toList());
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
