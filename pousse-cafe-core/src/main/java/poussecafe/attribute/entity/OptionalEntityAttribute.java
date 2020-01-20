package poussecafe.attribute.entity;

import poussecafe.attribute.OptionalAttribute;
import poussecafe.domain.Entity;

public interface OptionalEntityAttribute<E extends Entity<?, ?>> {

    OptionalAttribute<E> inContextOf(Entity<?, ?> primitive);

    E newInContextOf(Entity<?, ?> primitive);
}
