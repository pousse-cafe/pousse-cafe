package poussecafe.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
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

    public void setApplicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
        this.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    /**
     * @deprecated use getOptional instead.
     */
    @Deprecated(since = "0.8.0")
    public A find(K id) {
        return getOptional(id).orElse(null);
    }

    public Optional<A> getOptional(K id) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("get(" + entityClass.getSimpleName() + ")");
        try {
            checkId(id);
            return wrapNullable(dataAccess.findData(id));
        } finally {
            span.end();
        }
    }

    private EntityDataAccess<K, D> dataAccess;

    protected Optional<A> getOptional(String queryName, Supplier<D> query) {
        Objects.requireNonNull(query);
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(queryName);
        try {
            return wrapNullable(query.get());
        } finally {
            span.end();
        }
    }

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
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("add(" + entityClass.getSimpleName() + ")");
        try {
            checkEntity(entity);
            entity.onAdd();
            addData(entity);
        } finally {
            span.end();
        }
    }

    protected void addData(A entity) {
        MessageCollection messageCollection = entity.messageCollection();
        if(!entity.dontPersist()) {
            messageCollectionValidator.validate(messageCollection);
            D data = entity.attributes();
            dataAccess.addData(data);
        }
        considerMessageSending(entity, messageCollection);
    }

    private void considerMessageSending(A entity,
            MessageCollection messageCollection) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("sendMessages");
        try {
            entity.storage().getMessageSendingPolicy().considerSending(messageCollection);
        } finally {
            span.end();
        }
    }

    private void checkEntity(A entity) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(entity.attributes());
    }

    public void update(A entity) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("update(" + entityClass.getSimpleName() + ")");
        try {
            checkEntity(entity);
            entity.onUpdate();
            updateData(entity);
        } finally {
            span.end();
        }
    }

    protected void updateData(A entity) {
        MessageCollection messageCollection = entity.messageCollection();
        if(!entity.dontPersist()) {
            messageCollectionValidator.validate(messageCollection);
            D data = entity.attributes();
            dataAccess.updateData(data);
        }
        considerMessageSending(entity, messageCollection);
    }

    public void delete(K id) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("deleteByKey(" + entityClass.getSimpleName() + ")");
        try {
            checkId(id);
            Optional<A> entity = getOptional(id);
            if (entity.isPresent()) {
                deleteWithoutSpan(entity.get());
            }
        } finally {
            span.end();
        }
    }

    private void deleteWithoutSpan(A entity) {
        entity.onDelete();
        MessageCollection messageCollection = entity.messageCollection();
        messageCollectionValidator.validate(messageCollection);
        dataAccess.deleteData(entity.attributes().identifier().value());
        considerMessageSending(entity, messageCollection);
    }

    public void delete(A entity) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("delete(" + entityClass.getSimpleName() + ")");
        try {
            deleteWithoutSpan(entity);
        } finally {
            span.end();
        }
    }

    protected List<A> find(String queryName, Supplier<List<D>> query) {
        Objects.requireNonNull(query);
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(queryName);
        try {
            return wrap(query.get());
        } finally {
            span.end();
        }
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
