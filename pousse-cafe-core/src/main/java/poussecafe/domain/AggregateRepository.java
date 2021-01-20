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

public abstract class AggregateRepository<K, A extends AggregateRoot<K, D>, D extends EntityAttributes<K>> {

    @SuppressWarnings("unchecked")
    public void setEntityClass(Class<?> entityClass) {
        Objects.requireNonNull(entityClass);
        this.entityClass = (Class<A>) entityClass;
    }

    private Class<A> entityClass;

    public Class<A> entityClass() {
        return entityClass;
    }

    public void setApplicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
        this.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

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
            entity.validateIssuedMessages();
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
            entity.validateIssuedMessages();
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
                deleteInSpan(entity.get());
            }
        } finally {
            span.end();
        }
    }

    private void deleteInSpan(A entity) {
        entity.onDelete();
        MessageCollection messageCollection = entity.messageCollection();
        dataAccess.deleteData(entity.attributes().identifier().value());
        considerMessageSending(entity, messageCollection);
    }

    public void delete(A entity) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("delete(" + entityClass.getSimpleName() + ")");
        try {
            deleteInSpan(entity);
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

    public boolean existsById(K id) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName("existsById(" + entityClass.getSimpleName() + ")");
        try {
            return dataAccess.existsById(id);
        } finally {
            span.end();
        }
    }
}
