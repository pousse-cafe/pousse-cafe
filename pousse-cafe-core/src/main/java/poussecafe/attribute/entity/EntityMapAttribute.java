package poussecafe.attribute.entity;

import java.util.Objects;
import poussecafe.attribute.MapAttribute;
import poussecafe.domain.Entity;

public interface EntityMapAttribute<K, E extends Entity<K, ?>> {

    MapAttribute<K, E> inContextOf(Entity<?, ?> primitive);

    E newInContextOf(Entity<?, ?> primitive);

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

        public E inContextOf(Entity<?, ?> primitive) {
            MapAttribute<K, E> map = attribute.inContextOf(primitive);
            if(map.value().containsKey(key)) {
                throw new IllegalArgumentException("Map alreay contains an entity with ID " + key);
            } else {
                E newEntity = attribute.newInContextOf(primitive);
                newEntity.attributes().identifier().value(key);
                map.value().put(key, newEntity);
                return newEntity;
            }
        }
    }
}
