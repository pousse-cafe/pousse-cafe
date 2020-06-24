package poussecafe.attribute.entity;

import poussecafe.attribute.OptionalAttribute;
import poussecafe.domain.Entity;

public interface OptionalEntityAttribute<E extends Entity<?, ?>> extends OptionalAttribute<E> {

    @Deprecated(since = "0.20")
    OptionalAttribute<E> inContextOf(Entity<?, ?> primitive);

    @Deprecated(since = "0.20")
    E newInContextOf(Entity<?, ?> primitive);
}
