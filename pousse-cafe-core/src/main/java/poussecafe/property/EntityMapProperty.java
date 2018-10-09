package poussecafe.property;

import poussecafe.domain.Entity;
import poussecafe.domain.Component;

public interface EntityMapProperty<K, E extends Entity<K, ?>> {

    MapProperty<K, E> inContextOf(Component primitive);

    E newInContextOf(Component primitive);
}
