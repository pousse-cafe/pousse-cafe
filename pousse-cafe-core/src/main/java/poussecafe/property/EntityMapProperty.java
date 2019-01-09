package poussecafe.property;

import poussecafe.domain.Entity;

public interface EntityMapProperty<K, E extends Entity<K, ?>> {

    MapProperty<K, E> inContextOf(Entity<?, ?> primitive);

    E newInContextOf(Entity<?, ?> primitive);
}
