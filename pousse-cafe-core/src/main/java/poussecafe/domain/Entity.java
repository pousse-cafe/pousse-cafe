package poussecafe.domain;

import poussecafe.property.MessageCollection;
import poussecafe.storage.Storage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Checks.checkThatValue;

public abstract class Entity<K, D extends EntityData<K>> extends Component {

    private D data;

    @SuppressWarnings("unchecked")
    void setData(Object data) {
        checkThat(value(data).notNull().because("Data cannot be null"));
        this.data = (D) data;
    }

    public D getData() {
        return data;
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

    public void parent(Component parent) {
        checkThatValue(parent).notNull();
        this.parent = parent;
    }

    private Component parent;

    public Component parent() {
        return parent;
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

    @Override
    public <T> T newComponent(ComponentSpecification<T> specification) {
        T newPrimitive = super.newComponent(specification);
        if(newPrimitive instanceof Entity) {
            @SuppressWarnings("rawtypes")
            Entity entity = (Entity) newPrimitive;
            entity.storage(storage);
            entity.messageCollection(messageCollection);
        }
        return newPrimitive;
    }

    protected void addDomainEvent(DomainEvent event) {
        messageCollection().addMessage(event);
    }

    protected <E> E newDomainEvent(Class<E> eventClass) {
        return newComponent(eventClass);
    }
}
