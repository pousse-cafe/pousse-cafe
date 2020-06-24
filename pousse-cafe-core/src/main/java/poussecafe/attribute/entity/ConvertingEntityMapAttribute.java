package poussecafe.attribute.entity;

import java.util.Map;
import java.util.Objects;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.map.AdaptingMapAttribute;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;
import poussecafe.runtime.ActiveAggregate;

public abstract class ConvertingEntityMapAttribute<L, F extends EntityAttributes<K>, K, E extends Entity<K, ?>>
extends AdaptingMapAttribute<L, F, K, E>
implements EntityMapAttribute<K, E> {

    public ConvertingEntityMapAttribute(Map<L, F> data, Class<E> primitiveClass) {
        super(data);

        Objects.requireNonNull(primitiveClass);
        this.primitiveClass = primitiveClass;

        aggregateRoot = ActiveAggregate.instance().get();
    }

    private Class<E> primitiveClass;

    @Override
    protected E convertFromValue(F from) {
        E entity = newEntity();
        entity.attributes(from);
        return entity;
    }

    @SuppressWarnings("unchecked")
    private E newEntity() {
        return (E) aggregateRoot.newEntityBuilder(primitiveClass).buildWithoutId();
    }

    @SuppressWarnings("rawtypes")
    private AggregateRoot aggregateRoot;

    @SuppressWarnings("unchecked")
    @Override
    protected F convertToValue(E from) {
        return (F) from.attributes();
    }

    @Override
    public MapAttribute<K, E> inContextOf(Entity<?, ?> primitive) {
        return this;
    }

    @Override
    protected abstract L convertToKey(K from);

    @Override
    protected abstract K convertFromKey(L from);

    @Override
    public E newInContextOf(Entity<?, ?> primitive) {
        return newEntity();
    }

    @Override
    public EditableEntityMap<K, E> value() {
        return new EntityMap<>(super.value());
    }
}
