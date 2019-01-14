package poussecafe.domain;

import poussecafe.collection.MultiTypeMap;
import poussecafe.property.MessageCollection;
import poussecafe.storage.Storage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Checks.checkThatValue;

public abstract class Entity<K, D extends EntityData<K>> {

    @SuppressWarnings("unchecked")
    public void setData(Object data) {
        checkThat(value(data).notNull().because("Data cannot be null"));
        this.data = (D) data;
    }

    private D data;

    public D getData() {
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

    public K getKey() {
        return getData().key().get();
    }

    public void setKey(K key) {
        checkThatValue(key).notNull();
        getData().key().set(key);
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

    protected void addDomainEvent(DomainEvent event) {
        messageCollection().addMessage(event);
    }

    protected <E extends DomainEvent> E newDomainEvent(Class<E> eventClass) {
        return componentFactory.newMessage(eventClass);
    }

    private MultiTypeMap<String> transitiveContext = new MultiTypeMap<>();

    public MultiTypeMap<String> transitiveData() {
        return transitiveContext;
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
