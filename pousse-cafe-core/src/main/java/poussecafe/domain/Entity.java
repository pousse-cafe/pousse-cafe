package poussecafe.domain;

import java.util.Objects;
import poussecafe.attribute.entity.EntityAttribute;
import poussecafe.environment.EntityFactory;
import poussecafe.environment.MessageFactory;
import poussecafe.environment.NewEntityInstanceSpecification;
import poussecafe.storage.MessageCollection;
import poussecafe.storage.Storage;

public abstract class Entity<I, D extends EntityAttributes<I>> {

    @SuppressWarnings("unchecked")
    public void attributes(Object data) {
        Objects.requireNonNull(data);
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

    /**
     * @deprecated This feature was being used to prevent the persistence of changed applied to an Aggregate but still
     * issue events. Instead, it is recommended to check first if the state of the Aggregate must be updated and issue
     * events accordingly.
     */
    @Deprecated(forRemoval = true)
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

    /**
     * @deprecated use issue(DomainEvent) instead.
     */
    @Deprecated(since = "0.17")
    public void emitDomainEvent(DomainEvent event) {
        issue(event);
    }

    public void issue(DomainEvent event) {
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

    /**
     * @deprecated use newEntityBuilder()
     */
    @Deprecated(since = "0.20")
    public <E extends Entity<?, ?>> E newEntity(Class<E> entityClass) {
        E entity = entityFactory.newEntity(new NewEntityInstanceSpecification.Builder<E>()
                .entityClass(entityClass)
                .instantiateData(true)
                .build());
        entity.storage(storage);
        entity.messageCollection(messageCollection);
        return entity;
    }

    /**
     * @deprecated use newEntityBuilder() and set directly attribute's value
     */
    @Deprecated(since = "0.20")
    public <K, E extends Entity<K, ?>> Setter<K, E> setNew(EntityAttribute<E> attribute) {
        return new Setter<>(attribute);
    }

    public class Setter<K, E extends Entity<K, ?>> {

        public Setter(EntityAttribute<E> attribute) {
            this.attribute = attribute;
        }

        private EntityAttribute<E> attribute;

        /**
         * @deprecated use withId instead.
         */
        @Deprecated(since = "0.18.0")
        public E withKey(K key) {
            return withId(key);
        }

        @SuppressWarnings("unchecked")
        public E withId(K id) {
            E newEntity = (E) newEntityBuilder(attribute.entityClass()).buildWithoutId();
            newEntity.attributes().identifier().value(id);
            attribute.value(newEntity);
            return newEntity;
        }
    }

    public <J, F extends EntityAttributes<J>, E extends Entity<J, F>>
    EntityBuilder<J, F, E> newEntityBuilder(Class<E> entityClass) {
        return new EntityBuilder<J, F, E>(entityFactory).withType(entityClass);
    }
}
