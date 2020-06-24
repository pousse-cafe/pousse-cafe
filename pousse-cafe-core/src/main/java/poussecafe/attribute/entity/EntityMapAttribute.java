package poussecafe.attribute.entity;

import java.util.Objects;
import poussecafe.attribute.MapAttribute;
import poussecafe.domain.Entity;

public interface EntityMapAttribute<K, E extends Entity<K, ?>> extends MapAttribute<K, E> {

    @Deprecated(since = "0.20")
    MapAttribute<K, E> inContextOf(Entity<?, ?> primitive);

    @Deprecated(since = "0.20")
    E newInContextOf(Entity<?, ?> primitive);

    @Deprecated(since = "0.20")
    default Putter<K, E> putNew(K key) {
        Objects.requireNonNull(key);
        return new Putter<>(key, this);
    }

    public class Putter<K, E extends Entity<K, ?>> {

        private Putter(K key, EntityMapAttribute<K, E> attribute) {
            this.key = key;
            this.attribute = attribute;
        }

        private K key;

        private EntityMapAttribute<K, E> attribute;

        @Deprecated(since = "0.20")
        public E inContextOf(Entity<?, ?> primitive) {
            if(attribute.value().containsKey(key)) {
                throw new IllegalArgumentException("Map alreay contains an entity with ID " + key);
            } else {
                E newEntity = attribute.newInContextOf(primitive);
                newEntity.attributes().identifier().value(key);
                attribute.value().put(key, newEntity);
                return newEntity;
            }
        }
    }
}
