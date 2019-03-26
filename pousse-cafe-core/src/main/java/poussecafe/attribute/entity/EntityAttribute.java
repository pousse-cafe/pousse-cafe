package poussecafe.attribute.entity;

import poussecafe.attribute.Attribute;
import poussecafe.domain.Entity;

public interface EntityAttribute<E extends Entity<?, ?>> {

    Attribute<E> inContextOf(Entity<?, ?> primitive);

    E newInContextOf(Entity<?, ?> primitive);
}
