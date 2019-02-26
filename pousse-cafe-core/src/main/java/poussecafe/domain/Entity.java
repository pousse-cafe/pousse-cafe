package poussecafe.domain;

import java.util.Objects;
import poussecafe.environment.EntityFactory;
import poussecafe.environment.MessageFactory;
import poussecafe.storage.Storage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class Entity<K, D extends EntityAttributes<K>> {

    @SuppressWarnings("unchecked")
    public void attributes(Object data) {
        checkThat(value(data).notNull().because("Data cannot be null"));
        this.attributes = (D) data;
    }

    private D attributes;

    public D attributes() {
        return attributes;
    }

    public void entityFactory(EntityFactory entityFactory) {
        Objects.requireNonNull(entityFactory);
        this.entityFactory = entityFactory;
    }

    private EntityFactory entityFactory;

    protected EntityFactory entityFactory() {
        return entityFactory;
    }

    public void messageFactory(MessageFactory messageFactory) {
        Objects.requireNonNull(messageFactory);
        this.messageFactory = messageFactory;
    }

    private MessageFactory messageFactory;

    protected MessageFactory messageFactory() {
        return messageFactory;
    }

    protected void dontPersist(boolean value) {
        dontPersist = value;
    }

    private boolean dontPersist;

    public boolean dontPersist() {
        return dontPersist;
    }

    public MessageCollection messageCollection() {
        return messageCollection;
    }

    private MessageCollection messageCollection;

    public void messageCollection(MessageCollection messageCollection) {
        this.messageCollection = messageCollection;
    }

    public Storage storage() {
        return storage;
    }

    private Storage storage;

    public void storage(Storage storage) {
        this.storage = storage;
    }

    public void addDomainEvent(DomainEvent event) {
        messageCollection().addMessage(event);
    }

    public <E extends DomainEvent> E newDomainEvent(Class<E> eventClass) {
        return messageFactory.newMessage(eventClass);
    }

    public void context(Object context) {
        this.context = context;
    }

    private Object context;

    @SuppressWarnings("unchecked")
    public <C> C context() {
        Objects.requireNonNull(context);
        return (C) context;
    }

    public <E extends Entity<?, ?>> E newEntity(Class<E> entityClass) {
        E entity = entityFactory.newEntity(new EntitySpecification.Builder<E>()
                .withComponentClass(entityClass)
                .withData(true)
                .build());
        entity.storage(storage);
        entity.messageCollection(messageCollection);
        return entity;
    }
}
