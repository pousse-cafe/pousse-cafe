package poussecafe.domain;

import java.util.Objects;
import poussecafe.storage.Storage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class Entity<K, D extends EntityData<K>> {

    @SuppressWarnings("unchecked")
    public void setData(Object data) {
        checkThat(value(data).notNull().because("Data cannot be null"));
        this.data = (D) data;
    }

    private D data;

    public D data() {
        return data;
    }

    public void setComponentFactory(ComponentFactory componentFactory) {
        checkThat(value(componentFactory).notNull());
        this.componentFactory = componentFactory;
    }

    private ComponentFactory componentFactory;

    protected ComponentFactory componentFactory() {
        return componentFactory;
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
        return componentFactory.newMessage(eventClass);
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
        E entity = componentFactory.newEntity(new EntitySpecification.Builder<E>()
                .withComponentClass(entityClass)
                .withData(true)
                .build());
        entity.storage(storage);
        entity.messageCollection(messageCollection);
        return entity;
    }
}
