package poussecafe.property;

import poussecafe.domain.Entity;
import poussecafe.domain.Component;

public interface EntityProperty<E extends Entity<?, ?>> {

    Property<E> inContextOf(Component primitive);

    E newInContextOf(Component primitive);
}
