package poussecafe.attribute.entity;

import poussecafe.attribute.Attribute;
import poussecafe.domain.Entity;

public interface EntityAttribute<E extends Entity<?, ?>> extends Attribute<E> {

    @Deprecated(since = "0.20")
    Attribute<E> inContextOf(Entity<?, ?> primitive);

    @Deprecated(since = "0.20")
    E newInContextOf(Entity<?, ?> primitive);
}
