package poussecafe.attribute.entity;

import java.util.Collection;
import java.util.Map.Entry;
import poussecafe.attribute.entity.EntityMapAttributeBuilder.Complete;
import poussecafe.attribute.map.ImmutableEntry;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;
import poussecafe.runtime.ActiveAggregate;

import static java.util.Objects.requireNonNull;

class AdaptingEntityCollectionBackedMapAttributeBuilder<U extends EntityAttributes<K>, K, E extends Entity<K, ?>>
implements Complete<K, E> {

    AdaptingEntityCollectionBackedMapAttributeBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;

        aggregateRoot = ActiveAggregate.instance().get();
    }

    Collection<U> collection;

    @Override
    public EntityMapAttribute<K, E> build() {
        requireNonNull(entityClass);
        requireNonNull(collection);

        return new CollectionBackedConvertingEntityMapAttribute<>(collection) {
            @SuppressWarnings("unchecked")
            @Override
            public E newInContextOf(Entity<?, ?> primitive) {
                return (E) aggregateRoot.newEntityBuilder(entityClass).buildWithoutId();
            }

            @Override
            protected Entry<K, E> convertFromValue(U from) {
                @SuppressWarnings("unchecked")
                E entity = (E) aggregateRoot.newEntityBuilder(entityClass).buildWithoutId();
                entity.attributes(from);
                return new ImmutableEntry<>(entity.attributes().identifier().value(), entity);
            }

            @SuppressWarnings("unchecked")
            @Override
            protected U convertToValue(Entry<K, E> from) {
                return (U) from.getValue().attributes();
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private AggregateRoot aggregateRoot;

    private Class<E> entityClass;
}
