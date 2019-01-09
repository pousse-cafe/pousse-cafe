package poussecafe.property;

import poussecafe.domain.Entity;

public interface EntityProperty<E extends Entity<?, ?>> {

    Property<E> inContextOf(Entity<?, ?> primitive);

    E newInContextOf(Entity<?, ?> primitive);
}
