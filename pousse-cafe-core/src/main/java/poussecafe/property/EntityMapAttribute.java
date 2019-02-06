package poussecafe.property;

import poussecafe.attribute.MapAttribute;
import poussecafe.domain.Entity;

public interface EntityMapAttribute<K, E extends Entity<K, ?>> {

    MapAttribute<K, E> inContextOf(Entity<?, ?> primitive);

    E newInContextOf(Entity<?, ?> primitive);
}
